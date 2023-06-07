package com.example.ridesync.Classes;
//AIzaSyBs_hOEFhxymvHXX1Ha7teQrOkssB-lI1o

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class DisplayMap extends Application {

    private static final String HTML = "<html>"
            + "<head>"
            + "<meta name='viewport' content='initial-scale=1.0, user-scalable=yes' />"
            + "<script type='text/javascript' src='https://maps.googleapis.com/maps/api/js?key=AIzaSyBs_hOEFhxymvHXX1Ha7teQrOkssB-lI1o'></script>"
            + "<script type='text/javascript'>"
            + "function initialize() {"
            + "    var mapOptions = { center: new google.maps.LatLng(33.6426875, 72.9901717), zoom: 12 };"
            + "    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);"
            + "    var riderMarker = new google.maps.Marker({ position: new google.maps.LatLng(33.8426875, 72.9901717), map: map, icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png' });"
            + "    var driverMarker = new google.maps.Marker({ position: new google.maps.LatLng(33.6426875, 73.9901717), map: map, icon: 'http://maps.google.com/mapfiles/ms/icons/red-dot.png' });"
            + "}"
            + "google.maps.event.addDomListener(window, 'load', initialize);"
            + "</script>"
            + "</head>"
            + "<body>"
            + "<div id='map-canvas' style='width: 500px; height: 500px;'></div>"
            + "</body>"
            + "</html>";

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(HTML);

        // Add a listener to the stateProperty of the webEngine
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Content has finished loading, so initialize the map
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("app", this); // Expose Java object to JavaScript
                webEngine.executeScript("initialize();"); // Call the initialize function
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Java method callable from JavaScript to log a message
    public void logMessage(String message) {
        System.out.println("JavaScript says: " + message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
