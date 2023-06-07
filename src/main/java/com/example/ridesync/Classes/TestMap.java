package com.example.ridesync.Classes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;



import java.net.URL;

public class TestMap extends Application {

    private static final String HTML_FILE = "/map_test.html";

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
       // webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");


        // Get the URL of the HTML file using getResource()
        URL url = getClass().getResource(HTML_FILE);

        if (url != null) {
            // Load the HTML file using the obtained URL
            webEngine.load(url.toExternalForm());

            StackPane root = new StackPane();
            root.getChildren().add(webView);
            System.out.println("Html file was not null");
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.err.println("Failed to load HTML file: " + HTML_FILE);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


