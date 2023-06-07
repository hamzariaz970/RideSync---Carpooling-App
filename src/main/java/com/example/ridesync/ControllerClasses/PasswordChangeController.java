package com.example.ridesync.ControllerClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class PasswordChangeController {
    @FXML
    private Label errorLabel;

    @FXML
    private TextField cms_idField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private Button updateButton, cancelButton;

    public void onUpdateButtonClicked(ActionEvent event) {
        Connection connection = null; //will hold connection
        PreparedStatement psInsert = null; // for inserting
        PreparedStatement psCheckUserExists = null; // for checking if user already exists
        ResultSet resultSet = null; //holding the results of queries
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync", "root", ""); // establishing connection
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE cms_id = ?"); // loading usernames
            psCheckUserExists.setString(1, String.valueOf(cms_idField.getText())); // giving method argument: 'username' to be checked
            resultSet = psCheckUserExists.executeQuery(); // executing query

            // returns false if CMS ID does not exist in database
            if (!resultSet.isBeforeFirst()) {
                System.out.println("User does not exist");
                errorLabel.setText("User does not exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (passwordField.getText().equals(confirmPasswordField.getText()))
        {
            try {
                // Update password in the database
                PreparedStatement psUpdatePassword = connection.prepareStatement("UPDATE users SET password = ? WHERE cms_id = ?");
                psUpdatePassword.setString(1, passwordField.getText());
                psUpdatePassword.setString(2, String.valueOf(cms_idField.getText()));
                int rowsAffected = psUpdatePassword.executeUpdate();

                if (rowsAffected > 0) {
                    // Password updated successfully
                    System.out.println("Password updated");
                    errorLabel.setText("Password updated");
                } else {
                    // No rows affected (CMS ID not found)
                    System.out.println("User does not exist");
                    errorLabel.setText("User does not exist");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            errorLabel.setText("Passwords do not match");
        }

    }

    public void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
