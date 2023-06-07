package com.example.ridesync.ControllerClasses;

import com.example.ridesync.Classes.DataBaseUtils;
import com.example.ridesync.Classes.GoogleMapsCoordinates;
import com.google.maps.model.GeocodingResult;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField tf_name, tf_cmsID, tf_address;
    @FXML
    private PasswordField tf_password, tf_confirmPassword;


    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<String> universityComboBox;
    @FXML
    private Button backButton; // ADD TO NEW FILE



    @Override@FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female", "Don't Specify"));
        universityComboBox.setItems(FXCollections.observableArrayList("NUST", "FAST", "Air University"));
        errorLabel.setText("");

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DataBaseUtils.changeScene(event, "/com/example/ridesync/startup-screen.fxml", "RideSync", null);
            }
        });
    }

    public void onSignUpClicked(ActionEvent event){
        if (tf_password.getText().equals(tf_confirmPassword.getText())) {
            if( isAlphabetsAndSpaces(tf_name.getText())) {
                if(isNumeric(tf_cmsID.getText())) {

                    String address = tf_address.getText();
                    GeocodingResult location = null;
                    try {
                        location = GoogleMapsCoordinates.returnCoordinates(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (location != null) {
                        if (!genderComboBox.getValue().toString().isEmpty()) {

                            DataBaseUtils.signUpUser(event,
                                    tf_name.getText(),
                                    tf_confirmPassword.getText(),
                                    genderComboBox.getValue().toString(),
                                    Integer.parseInt(tf_cmsID.getText()),
                                    universityComboBox.getValue().toString(),
                                    location);
                        }
                        else {
                            errorLabel.setText("Please select gender");
                        }
                    }
                    else {
                        errorLabel.setText("Incorrect Address entered.");
                    }
                }
                else {
                    errorLabel.setText("Incorrect CMS ID entered.");
                }
            }
            else {
                errorLabel.setText("Incorrect name entered.");
            }
        }
        else {
            errorLabel.setText("Password does not match. Enter again.");
        }
    }

    public static void setErrorLabel(String text) {
        SignUpController signUpController= new SignUpController();
        signUpController.errorLabel.setText(text);
    }



    public static boolean isAlphabetsAndSpaces(String str) {
        // check if string is null or empty
        if (str == null || str.isEmpty()) {
            return false;
        }
        // iterate through each character in the string
        for (char c : str.toCharArray()) {
            // check if the character is a space or an alphabet
            if (c != ' ' && !Character.isLetter(c)) {
                return false;
            }
        }
        // if all characters are either spaces or alphabets, return true
        return true;
    }

    private static boolean isNumeric(String str) {
        // check if string is empty
        if (str == null || str.length() == 0) {
            return false;
        }
        // iterate over each character
        for (int i = 0; i <str.length(); i++)
        {
            // check if character is a digit
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        // if all are digits, then return true
        return true;
    }



}



