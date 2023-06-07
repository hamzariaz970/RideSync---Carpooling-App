package com.example.ridesync.ControllerClasses;

import com.example.ridesync.Classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;



public class WalletPopupController {
    @FXML
    private TextField walletTextField;
    @FXML
    private Button addButton, cancelButton;
    @FXML
    private Label errorLabel, currentBalanceLabel;
    private User user;

    public void addButtonClicked(ActionEvent event) {

        String str = walletTextField.getText();
        int balance = 0;

        boolean isValidInput = true;
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                isValidInput = false;
                break;
            }
        }
        if (!isValidInput) {
            // Display an error message for invalid input
            errorLabel.setText("Invalid amount entered");
            errorLabel.setStyle("-fx-text-fill: #ff4242;");
            walletTextField.setText("");
        }

        try {
            // Try to parse the string as a integer
            balance = Integer.parseInt(str);

            if (balance > 5000) {
                errorLabel.setText("Please enter a lower amount. Max limit is Rs. 5000");
                errorLabel.setStyle("-fx-text-fill: #ff4242;");
            } else if (balance < 0) {
                errorLabel.setText("Invalid amount entered");
                errorLabel.setStyle("-fx-text-fill: #ff4242;");
                walletTextField.setText("");

            } else {

                Connection connection = null; //will hold connection
                ResultSet resultSet = null; //holding the results of queries
                PreparedStatement preparedStatement = null;
                int retrievedBalance = 0;
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync?useUnicode=true&characterEncoding=utf8", "root", ""); // establishing connection
                    preparedStatement = connection.prepareStatement("SELECT * FROM RideSync.users WHERE cms_id = ?");
                    preparedStatement.setInt(1, user.getCmsID());
                    resultSet = preparedStatement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        errorLabel.setText("Error. User not found.");
                        errorLabel.setStyle("-fx-text-fill: #ff4242;");
                        walletTextField.setText("");
                    }
                    while (resultSet.next()) {
                        retrievedBalance = resultSet.getInt("wallet_balance");
                    }

                    if (balance + retrievedBalance > 5000) {
                        errorLabel.setText("Please enter a lower amount. Max limit is Rs. 5000");
                        errorLabel.setStyle("-fx-text-fill: #ff4242;");
                        walletTextField.setText("");
                    } else {
                        // Perform the update operation here using the retrievedBalance and balance
                        int newBalance = retrievedBalance + balance;
                        // Update the balance in the database for the user with the specified cms_id
                        preparedStatement = connection.prepareStatement("UPDATE RideSync.users SET wallet_balance = ? WHERE cms_id = ?");
                        preparedStatement.setInt(1, newBalance);
                        preparedStatement.setInt(2, user.getCmsID());

                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {

                            errorLabel.setText("Balance updated successfully");
                            errorLabel.setStyle("-fx-text-fill: white;");
                        } else {
                            errorLabel.setText("Failed to update balance");
                            errorLabel.setStyle("-fx-text-fill: #ff4242;");
                            walletTextField.setText("");
                        }

                        currentBalanceLabel.setText(String.valueOf(user.fetchBalanceFromDatabase(user.getCmsID())));
                        walletTextField.setText("");

                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    //closing all connections
                    if (resultSet != null) {
                        try {
                            resultSet.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid amount entered");
            errorLabel.setStyle("-fx-text-fill: #ff4242;");
            walletTextField.setText("");
        }
    }

        public void cancelButtonClicked (ActionEvent event){
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }


        public void setUser (User user){
            this.user = user;
            currentBalanceLabel.setText("RS." + user.fetchBalanceFromDatabase(user.getCmsID()));
            currentBalanceLabel.setStyle("-fx-text-fill: green;");
        }


    }

