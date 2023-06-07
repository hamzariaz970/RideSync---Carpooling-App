package com.example.ridesync.ControllerClasses;

import com.example.ridesync.Classes.DataBaseUtils;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class StartupScreenController implements Initializable {

    @FXML
    private AnchorPane pane1, pane2;
    @FXML
    private Button loginButton, signUpButton, exitButton;
    @FXML
    private ImageView logoNameIcon;


   /* @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoNameIcon.setVisible(false);

        pane2.setTranslateY(+100);
        TranslateTransition slidePane2in = new TranslateTransition(Duration.seconds(0.2), pane2);
        slidePane2in.setToY(720);
        slidePane2in.setOnFinished(e -> {
            FadeTransition setLogoNameIconVisible = new FadeTransition(Duration.seconds(0.3), logoNameIcon);
            //logoNameIcon.setVisible(true);
        });;

        slidePane2in.play();


    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoNameIcon.setOpacity(0);

        pane2.setTranslateY(720); // Move pane2 below the screen

        TranslateTransition slidePane2In = new TranslateTransition(Duration.seconds(1), pane2);
        slidePane2In.setToY(0); // Move pane2 to its original position
        slidePane2In.setOnFinished(e -> {
            FadeTransition fadeInLogoNameIcon = new FadeTransition(Duration.seconds(1.5), logoNameIcon);
            fadeInLogoNameIcon.setFromValue(0); // Start with opacity 0
            fadeInLogoNameIcon.setToValue(1); // Fade in to opacity 1
            fadeInLogoNameIcon.play();
        });

        slidePane2In.play();
    }


    public void loginClicked(ActionEvent event) {
        DataBaseUtils.changeScene(event, "/com/example/ridesync/login-view.fxml", "Login", null);
    }

    public void signupClicked(ActionEvent event) {
        DataBaseUtils.changeScene(event, "/com/example/ridesync/sign-up-view.fxml", "Sign Up", null);

    }

    public void exitClicked(ActionEvent event) {
        Platform.exit();
    }



}
