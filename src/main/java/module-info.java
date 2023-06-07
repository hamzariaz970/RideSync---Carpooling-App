module com.example.ridesync {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.maps.services;
    requires javafx.web;
    requires jdk.jsobject;
    requires java.sql;


    opens com.example.ridesync to javafx.fxml;
    exports com.example.ridesync;
    exports com.example.ridesync.ControllerClasses;
    opens com.example.ridesync.ControllerClasses to javafx.fxml;
    exports com.example.ridesync.Classes;
    opens com.example.ridesync.Classes to javafx.fxml;
}