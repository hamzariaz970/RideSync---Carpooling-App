package com.example.ridesync.ControllerClasses;

import com.example.ridesync.Classes.DataBaseUtils;
import com.example.ridesync.Classes.DateTimeWidget;
import com.example.ridesync.Classes.Driver;
import com.example.ridesync.Classes.User;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.security.SecureRandom;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class DriverController implements Initializable {
    @FXML
    private AnchorPane pane1, pane2;
    @FXML
    private Button menu, logoutButton, homeButton, walletButton, startRideButton;
    private User user;
    @FXML
    private ImageView topBarImageView;

    @FXML
    private Label welcomeLabel;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private RadioButton homeSelectButton, universitySelectButton;
    @FXML
    private ComboBox hourCombobox, minuteComboBox;
    @FXML
    private Pane startRidePane;

    private DateTimeWidget dateTimeWidget;


    private boolean isPane1Visible = false;

    public void onLogoutClicked(ActionEvent event) throws IOException {
        DataBaseUtils.changeScene( event, "/com/example/ridesync/login-view.fxml", "Login Screen", null);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Create a Rectangle with rounded corners
        Rectangle clip = new Rectangle(topBarImageView.getFitWidth(), topBarImageView.getFitHeight());
        clip.setArcWidth(20); // Adjust the arc width to control the roundness
        clip.setArcHeight(20); // Adjust the arc height to control the roundness

        // Set the Rectangle as the clip for the ImageView
        topBarImageView.setClip(clip);


        // initially pane1 is set to be out of view
        pane1.setTranslateX(-260);

        //sliding pane1 into view
        TranslateTransition slideIn_pane1 = new TranslateTransition(Duration.seconds(0.25), pane1);
        slideIn_pane1.setToX(0);
        slideIn_pane1.setOnFinished(e -> {
            isPane1Visible = true;
        });

        // sliding pane1 out of view
        TranslateTransition slideOut_pane1 = new TranslateTransition(Duration.seconds(0.25), pane1);
        slideOut_pane1.setToX(-260);
        slideOut_pane1.setOnFinished(e -> {
            isPane1Visible = false;
        });


        TranslateTransition slideLeft_pane2 = new TranslateTransition(Duration.seconds(0.5), pane2);
        slideLeft_pane2.setToX(260);

        menu.setOnAction(e -> {
            if (!isPane1Visible) {
                slideIn_pane1.play();
            } else {
                slideOut_pane1.play();
            }
        });

        changeStartRidePaneSize();


        // Create an instance of the DateTimeWidget
        dateTimeWidget = new DateTimeWidget();

        // Wrap the DateTimeWidget in a Label
        Label dateTimeLabel = new Label();
        dateTimeLabel.setGraphic(dateTimeWidget);
        dateTimeLabel.setTranslateX(150);
        dateTimeLabel.setTranslateY(120);

        // Add the Label to the root pane
        pane2.getChildren().add(dateTimeLabel);
        dateTimeWidget.startUpdating();


        ToggleGroup toggleGroup = new ToggleGroup();
        homeSelectButton.setToggleGroup(toggleGroup);
        universitySelectButton.setToggleGroup(toggleGroup);

        hourCombobox.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6));
        minuteComboBox.setItems(FXCollections.observableArrayList(15, 30, 45));



    }
    @FXML
    public void pane2Clicked(MouseEvent event){
        if (isPane1Visible) {
            TranslateTransition slideOut_pane1 = new TranslateTransition(Duration.seconds(0.25), pane1);
            slideOut_pane1.setToX(-260);
            slideOut_pane1.setOnFinished(e -> {
                isPane1Visible = false;
            });
            slideOut_pane1.play();
        }

    }

    public void setUser(User user) {
        this.user = user;
        universitySelectButton.setText(user.getUniversity());
    }


    public void setWelcomeLabel()
    {
        welcomeLabel.setText("DRIVER MODE: " + user.getName());
    }


    public void homeButtonClicked(ActionEvent event){
        // sliding pane1 out of view
        TranslateTransition slideOut_pane1 = new TranslateTransition(Duration.seconds(0.15), pane1);
        slideOut_pane1.setToX(-260);
        slideOut_pane1.setOnFinished(e -> {
            isPane1Visible = false;
        });
        TranslateTransition slideLeft_pane2 = new TranslateTransition(Duration.seconds(0.5), pane2);
        slideLeft_pane2.setToX(260);
        slideOut_pane1.play();

        DataBaseUtils.changeScene(event,"/com/example/ridesync/logged-in-view.fxml", "RideSync", user);
    }



    public void walletButtonClicked(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ridesync/wallet-popup.fxml"));
            Parent root = (Parent) loader.load();
            WalletPopupController walletPopupController = loader.getController();
            walletPopupController.setUser(user);

            Stage stage = new Stage();
            stage.setTitle("Wallet Balance");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changeStartRidePaneSize() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), startRidePane);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        startRidePane.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        startRidePane.setOnMouseExited(event -> {
            scaleTransition.setRate(-1.0);
            scaleTransition.play();
        });
    }


        public void onStartRideButtonClicked(ActionEvent event) {
            System.out.println("Button click!");
            boolean Home = false;
            boolean university = false;

            if (homeSelectButton.isSelected())
            {
                Home = true;
            }
            else {
                Home = false;
            }
            if (universitySelectButton.isSelected())
            {
                university = true;
            }
            else
            {
                university = false;
            }

            String dest = "NUST";

            if (Home == true)
            {
                dest = "Home";
            }
            else {
                dest = "NUST";
            }

            String minute;
            String hour;
            if (hourCombobox.getValue()== null || minuteComboBox.getValue() == null )
            {
                hour = "00";
                minute = "30";
            }
            else {
                hour = hourCombobox.getValue().toString();
                minute = minuteComboBox.getValue().toString();
            }

           // ride_id += 1;

            System.out.println(hour + ":" + minute);

            String filename = "data.txt";

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(user.getName() + ",");
                writer.write(Driver.getVehicleFromDatabase(user.getCmsID()).getModel() +",");
                writer.write(String.valueOf(SecureRandomNumberGenerator.generateRandomNumber()) + " km"+",");
                writer.write(hour+":" + minute);

                System.out.println("Data saved to " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}






class SecureRandomNumberGenerator {

    public static double generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        double min = 0.5;
        double max = 5.0;
        double increment = 0.1;

        // Calculate the range and the number of possible values
        double range = max - min;
        int numPossibleValues = (int) (range / increment);

        // Generate a random integer between 0 and numPossibleValues
        int randomIndex = secureRandom.nextInt(numPossibleValues);

        // Calculate the random number using the random index and increment
        double randomNumber = min + randomIndex * increment;
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        randomNumber = Double.parseDouble(String.format("%.2f", randomNumber));

        return randomNumber;
    }
}




