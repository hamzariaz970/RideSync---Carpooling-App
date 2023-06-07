package com.example.ridesync.ControllerClasses;

import com.example.ridesync.Classes.*;
import com.example.ridesync.Classes.Driver;
import javafx.animation.*;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private AnchorPane pane1, pane2;
    @FXML
    private Button menu, logoutButton, searchRidesButton, homeButton;
    private User user;
    @FXML
    private ImageView topBarImageView;

    @FXML
    private Label welcomeLabel;
    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Pane scheduleRidesPane, availableRidesPane;

    @FXML
    private RadioButton homeSelectButton,universitySelectButton, maleGenderSelect, femaleGenderSelect, anyGenderSelect;
    private DateTimeWidget dateTimeWidget;


    @FXML
    private Label driver1NameLabel, driver1CarLabel, driver1DistanceLabel, driver1TimeLabel;
    @FXML
    private Label  driver2NameLabel, driver2CarLabel, driver2DistanceLabel, driver2TimeLabel; //second driver

    @FXML
    private Label  driver3NameLabel, driver3CarLabel, driver3DistanceLabel, driver3TimeLabel; //third driver

    @FXML
    private AnchorPane basePane;

    private static Driver tempDriver; // ADD TO NEW FILE


    private boolean isPane1Visible = false;

    public void onLogoutClicked(ActionEvent event) throws IOException {
        DataBaseUtils.changeScene( event, "/com/example/ridesync/login-view.fxml", "Login Screen", null);

    }
    ToggleGroup toggleGroup1 = new ToggleGroup();
    ToggleGroup toggleGroup2 = new ToggleGroup();

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

        changeScheduleRidePaneSize();
        changeAvailableRidePaneSize();

        // for location selection

        homeSelectButton.setToggleGroup(toggleGroup1);
        universitySelectButton.setToggleGroup(toggleGroup1);

        // for gender of driver selection

        maleGenderSelect.setToggleGroup(toggleGroup2);
        femaleGenderSelect.setToggleGroup(toggleGroup2);
        anyGenderSelect.setToggleGroup(toggleGroup2);

      availableRidesPane.setOpacity(0);


        toggleGroup1.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup1.getSelectedToggle() != null) {
                // Handle the selection in toggleGroup1
            }
        });

        // Listener for toggleGroup2
        toggleGroup2.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup2.getSelectedToggle() != null) {
                // Handle the selection in toggleGroup2
            }
        });

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
        universitySelectButton.setText(universitySelectButton.getText() + " " + user.getUniversity());

    }


    public void setWelcomeLabel()
    {
        welcomeLabel.setText("Welcome, " + user.getName() + "!");
    }



    public void toggleButtonActivated(ActionEvent event) {
        if (isUserRegisteredAsDriver()) {
            System.out.println("User registered as driver");
            // User is already registered as a driver, create a Driver object and switch to the driver screen
            Vehicle vehicle = Driver.getVehicleFromDatabase(user.getCmsID());
            Driver driver = new Driver(user.getName(), user.getCmsID(), user.getUniversity(), user.getAddress(), vehicle);
            DataBaseUtils.changeScene(event, "/com/example/ridesync/driver-view.fxml", "RideSync", driver);
        } else {
            // User is not registered as a driver, open the vehicle registration popup
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ridesync/register-vehicle-popup.fxml"));
                Parent root = (Parent) loader.load();
                RegisterVehicleController registerVehicleController = loader.getController();
                registerVehicleController.setUser(user);

                Stage stage = new Stage();
                stage.setTitle("Vehicle Registration");
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                // Set a callback for the register button in the popup window
                registerVehicleController.setRegisterButtonCallback(() -> {
                    stage.close(); // Close the popup window
                    Stage currentStage = (Stage) toggleButton.getScene().getWindow();
                    currentStage.close(); // Close the current window
                });

                stage.showAndWait();
                // ADD TO NEW FILE
                // Switch the scene to the driver window
                DataBaseUtils.changeScene(event, "/com/example/ridesync/driver-view.fxml", "RideSync", tempDriver);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


   private boolean isUserRegisteredAsDriver() {
       Connection connection = null;
       PreparedStatement preparedStatement = null;
       ResultSet resultSet = null;

       try {
           connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync?useUnicode=true&characterEncoding=utf8", "root", "");
           preparedStatement = connection.prepareStatement("SELECT * FROM drivers WHERE cms_id = ?");
           preparedStatement.setInt(1, user.getCmsID());
           resultSet = preparedStatement.executeQuery();

           return resultSet.next() && resultSet.getString("vehicle_license") != null;
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           // Closing connections
           if (resultSet != null) {
               try {
                   resultSet.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
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

       return false; // Default to false if an exception occurs
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


    public void changeScheduleRidePaneSize() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), scheduleRidesPane);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        scheduleRidesPane.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        scheduleRidesPane.setOnMouseExited(event -> {
            scaleTransition.setRate(-1.0);
            scaleTransition.play();
        });
    }


    public void changeAvailableRidePaneSize() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), availableRidesPane);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        availableRidesPane.setOnMouseEntered(event -> {
            scaleTransition.playFromStart();
        });

        availableRidesPane.setOnMouseExited(event -> {
            scaleTransition.setRate(-1.0);
            scaleTransition.play();
        });
    }

   /* public void onSearchRidesButtonClicked(ActionEvent event) {
        System.out.println("Button clicked");
        toggleGroup1.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup1.getSelectedToggle() != null) {
                toggleGroup2.selectedToggleProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (toggleGroup1.getSelectedToggle() != null && toggleGroup2.getSelectedToggle() != null) {
                        FadeTransition fadeInLogoNameIcon = new FadeTransition(Duration.seconds(0.75), availableRidesPane);
                        fadeInLogoNameIcon.setFromValue(0); // Start with opacity 0
                        fadeInLogoNameIcon.setToValue(1); // Fade in to opacity 1
                        fadeInLogoNameIcon.play();

                        String filename = "data.txt";
                        String name = "";
                        String vehicleModel = "";
                        String distance = "";
                        String leavingTime = "";

                        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                            String line = reader.readLine();

                            if (line != null) {
                                String[] parts = line.split(",");

                                if (parts.length == 4) {
                                    name = parts[0];
                                    vehicleModel = parts[1];
                                    distance = parts[2];
                                    leavingTime = parts[3];

                                    System.out.println("Data read from " + filename);
                                    System.out.println("Name: " + name);
                                    System.out.println("Vehicle Model: " + vehicleModel);
                                    System.out.println("Distance: " + distance);
                                    System.out.println("Leaving Time: " + leavingTime);
                                } else {
                                    System.out.println("Invalid data format in the file.");
                                }
                            } else {
                                System.out.println("File is empty.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        setDriver1NameLabel(name);
                        setDriver1CarLabel(vehicleModel);
                        setDriver1DistanceLabel(distance);
                        setDriver1TimeLabel(leavingTime);
                    }
                });
            } else {System.out.println("Choose options!"); }
        });

    }*/



    public void onSearchRidesButtonClicked(ActionEvent event) {
        System.out.println("Button clicked");

        if (toggleGroup1.getSelectedToggle() != null && toggleGroup2.getSelectedToggle() != null) {
            FadeTransition fadeInLogoNameIcon = new FadeTransition(Duration.seconds(0.75), availableRidesPane);
            fadeInLogoNameIcon.setFromValue(0); // Start with opacity 0
            fadeInLogoNameIcon.setToValue(1); // Fade in to opacity 1
            fadeInLogoNameIcon.play();

            String filename = "data.txt";
            String name = "";
            String vehicleModel = "";
            String distance = "";
            String leavingTime = "";

            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line = reader.readLine();

                if (line != null) {
                    String[] parts = line.split(",");

                    if (parts.length == 4) {
                        name = parts[0];
                        vehicleModel = parts[1];
                        distance = parts[2];
                        leavingTime = parts[3];

                        System.out.println("Data read from " + filename);
                        System.out.println("Name: " + name);
                        System.out.println("Vehicle Model: " + vehicleModel);
                        System.out.println("Distance: " + distance);
                        System.out.println("Leaving Time: " + leavingTime);
                    } else {
                        System.out.println("Invalid data format in the file.");
                    }
                } else {
                    System.out.println("File is empty.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            setDriver1NameLabel(name);
            setDriver1CarLabel(vehicleModel);
            setDriver1DistanceLabel(distance);
            setDriver1TimeLabel(leavingTime);
        } else {
            System.out.println("Choose options!");
        }
    }





    public void setDriver1NameLabel(String text) {
        driver1NameLabel.setText(text);
    }

    public void setDriver1CarLabel (String text) {
        driver1CarLabel.setText(text);
    }
    public void setDriver1DistanceLabel(String text) {
        driver1DistanceLabel.setText(text);
    }

    public void setDriver1TimeLabel(String text) {
        driver1TimeLabel.setText(text);
    }



    // ADD T0 NEW FILE
    public static void setTempDriver(Driver driver)
    {
        tempDriver = driver;
    }
    // ADD TO NEW FILE
    public void homeButtonClicked(ActionEvent event) {
        TranslateTransition slideOut_pane1 = new TranslateTransition(Duration.seconds(0.15), pane1);
        slideOut_pane1.setToX(-260);
        slideOut_pane1.setOnFinished(e -> {
            isPane1Visible = false;
        });
        slideOut_pane1.play();
    }


}








