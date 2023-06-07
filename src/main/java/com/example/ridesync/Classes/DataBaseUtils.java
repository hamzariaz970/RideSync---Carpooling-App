package com.example.ridesync.Classes;


import com.example.ridesync.ControllerClasses.DriverController;
import com.example.ridesync.ControllerClasses.LoggedInController;
import com.example.ridesync.ControllerClasses.LoginController;
import com.example.ridesync.ControllerClasses.SignUpController;
import com.google.maps.model.GeocodingResult;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

// ADD DETAILS OF YOUR DATABASE IN THE signUpUser and loginUser methods

public class DataBaseUtils {

    // method to change scenes

    public static <T> void changeScene(ActionEvent event, String fxmlFile, String title, User user) {
        // setting Parent root to null initially
        Parent root;
                try {
                // loading the given fxml file
                    FXMLLoader loader = new FXMLLoader(DataBaseUtils.class.getResource(fxmlFile));
                root = loader.load();
                if (user != null)
                {
                   /* LoggedInController loggedInController = loader.getController();
                    loggedInController.setUser(user);
                    loggedInController.setWelcomeLabel();*/
                        T controller = loader.getController();
                        if (controller instanceof LoggedInController) {
                            LoggedInController loggedInController = (LoggedInController) controller;
                            loggedInController.setUser(user);
                            loggedInController.setWelcomeLabel();
                        }
                        else if (controller instanceof DriverController) {
                            DriverController driverController = (DriverController) controller;
                            driverController.setUser(user);
                            driverController.setWelcomeLabel();
                        }
                }

                // creating a stage to hold the fxml file
                // event obtained from getSource(). Then scene is obtained and then its window is obtained.
                // It is type cast to a node, which is cast to a stage, that can be displayed in a window
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setTitle(title);
                    // Create a fade transition
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), root);
                    fadeTransition.setFromValue(0.0);
                    fadeTransition.setToValue(1.0);



                stage.setScene(new Scene(root));
                fadeTransition.play();
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }



    // method to sign up a new user
     public static void signUpUser(ActionEvent event, String username, String password, String gender, int cmsID, String university, GeocodingResult location) {
        Connection connection = null; //will hold connection
        PreparedStatement psInsert = null; // for inserting
        PreparedStatement psCheckUserExists = null; // for checking if user already exists
        ResultSet resultSet = null; //holding the results of queries

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync", "root", ""); // establishing connection
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?"); // loading usernames
            psCheckUserExists.setString(1, username); // giving method argument: 'username' to be checked
            resultSet = psCheckUserExists.executeQuery(); // executing query

            // returns true if username already exists in database
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                SignUpController.setErrorLabel("User already exists!");

            } else {
                if (username.length() >= 44){
                    username = username.trim().substring(0,44);
                }
                if (location.formattedAddress.length() >= 99){
                    location.formattedAddress = location.formattedAddress.trim().substring(0,99);
                }
                String[] nameParts = username.split(" ");
                StringBuilder capitalizedFullName = new StringBuilder();

                for (String namePart : nameParts) {
                    String capitalizedPart = namePart.substring(0, 1).toUpperCase() + namePart.substring(1);
                    capitalizedFullName.append(capitalizedPart).append(" ");
                }


                psInsert = connection.prepareStatement("INSERT INTO users (cms_id, username, gender, password, address, university, wallet_balance) VALUES (?, ?, ?, ?, ?, ?, ? )");
                psInsert.setString(1, String.valueOf(cmsID));
                psInsert.setString(2, capitalizedFullName.toString().trim());
                psInsert.setString(3, gender);
                psInsert.setString(4, password.trim());
                psInsert.setString(5, location.formattedAddress.trim());
                psInsert.setString(6, university);
                psInsert.setString(7, String.valueOf(500));
                psInsert.executeUpdate();

                System.out.println("User added to database");
                User user = new User(username, cmsID, university, location.formattedAddress);

                DataBaseUtils.changeScene(event, "/com/example/ridesync/logged-in-view.fxml", "RideSync", user);

            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally { //closing all connections
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (psInsert != null) {
                try {
                    psInsert.close();
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


    public static void loginUser(ActionEvent event, int cmsID, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync?useUnicode=true&characterEncoding=utf8", "root", ""); // establishing connection
            preparedStatement = connection.prepareStatement("SELECT * FROM RideSync.users WHERE cms_id = ?");
            preparedStatement.setInt(1, cmsID);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()) {
                LoginController.setLoginWarningLabel("Error. User not found.");
            }
            // if user found
            else {
                // comparing with each column to get password column and row
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    int retrievedCMSID = resultSet.getInt("cms_id");
                    // if password matches
                    if (retrievedPassword.equals(password)) {
                        String username = resultSet.getString("username");
                        // changing scene to logged in
                        System.out.println("User log in successful");
                        User user = new User(username, cmsID, resultSet.getString("university"), resultSet.getString("address"));
                        DataBaseUtils.changeScene(event, "/com/example/ridesync/logged-in-view.fxml", "RideSync", user);
                    }
                    else {
                        // show warning on label
                        LoginController.setLoginWarningLabel("Incorrect password entered.");

                    }
                }
            }
        }catch (SQLException e) {
             e.printStackTrace();
        } finally { //closing all connections
            if (resultSet == null)
            {   try {
                resultSet.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }

            if (preparedStatement == null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection == null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            }
        }


    }




}
