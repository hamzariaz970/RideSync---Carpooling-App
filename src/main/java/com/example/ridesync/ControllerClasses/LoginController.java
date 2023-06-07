package com.example.ridesync.ControllerClasses;

import com.example.ridesync.Classes.DataBaseUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    private Button signInButton;
    @FXML
    private Label loginWarningLabel;
    @FXML
    private TextField tf_cmsID;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Button backButton;

    @FXML
    private Button forgotPasswordButton;// ADD TO NEW FILE

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DataBaseUtils.changeScene(event, "/com/example/ridesync/startup-screen.fxml", "RideSync", null);
            }
        });

    }


    public void signInButtonOnAction(ActionEvent event)
    {

        if ((tf_cmsID.getText().isBlank()) && (tf_password.getText().isBlank()))
        {
            loginWarningLabel.setText("Please enter username and password.");
        }
        else{
            try {
                DataBaseUtils.loginUser(event, Integer.parseInt(tf_cmsID.getText()), tf_password.getText());
            } catch (Exception e) {
                loginWarningLabel.setText("Incorrect username or password entered");
            }
        }


    }


    public static void setLoginWarningLabel(String text) {
        LoginController loginController = new LoginController();
        loginController.loginWarningLabel.setText(text);
    }

    // ADD TO NEW FILE
    public void forgotPasswordClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ridesync/password-change-popup.fxml"));
            Parent root = (Parent) loader.load();
          PasswordChangeController passwordChangeController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}






