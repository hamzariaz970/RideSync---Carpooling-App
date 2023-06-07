package com.example.ridesync.ControllerClasses;

import com.example.ridesync.Classes.Driver;
import com.example.ridesync.Classes.User;
import com.example.ridesync.Classes.Vehicle;
import com.example.ridesync.Classes.DataBaseUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterVehicleController {
    private User user;
    private Runnable registerButtonCallback;
    @FXML
    private TextField carModelTextField, carlLicenseTextField, carSeatsTextField;
    @FXML
    private Label errorLabel;

    @FXML
    private Button registerButton, cancelButton;


    public void onRegisterButtonClicked(ActionEvent event) {
        String model = carModelTextField.getText();
        String licensePlate = carlLicenseTextField.getText();
        int seatsAvailable = 4;
        try {
            seatsAvailable = Integer.parseInt(carSeatsTextField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("Incorrect seats entered");
            e.printStackTrace();
        }
        if (seatsAvailable > 4) {
            errorLabel.setText("Incorrect seats entered");
        } else {
            // Create a Vehicle object with the entered details
            Vehicle vehicle = new Vehicle(model, licensePlate, seatsAvailable);

            // Create a Driver object with the User and Vehicle objects
            Driver driver = new Driver(user.getName(), user.getCmsID(), user.getUniversity(), user.getAddress(), vehicle);
            // ADD TO NEW FILE
            LoggedInController.setTempDriver(driver);

            // Insert the driver data into the database
            insertDriverIntoDatabase(driver);
             Stage stage = (Stage) cancelButton.getScene().getWindow();
             stage.close();


        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void insertDriverIntoDatabase(Driver driver) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync?useUnicode=true&characterEncoding=utf8", "root", "");
            preparedStatement = connection.prepareStatement("INSERT INTO drivers (cms_id, vehicle_model, vehicle_license, seats_available) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, driver.getCmsID());
            preparedStatement.setString(2, driver.getVehicle().getModel());
            preparedStatement.setString(3, driver.getVehicle().getLicensePlate());
            preparedStatement.setInt(4, driver.getVehicle().getSeatsAvailable());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Closing connections
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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


    public void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setRegisterButtonCallback(Runnable callback) {
        this.registerButtonCallback = callback;
    }



}
