package com.example.ridesync.Classes;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeWidget extends StackPane {

    private Label dateTimeLabel;
    private Timeline timeline;

    public DateTimeWidget() {
        dateTimeLabel = new Label();
        dateTimeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        dateTimeLabel.setStyle("-fx-text-fill: #333333;");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = formatDateWithOrdinal(now) + "\n\t\t" + formatTime(now);
            dateTimeLabel.setText(formattedDateTime);
        }));

        timeline.setCycleCount(Animation.INDEFINITE);

        setAlignment(Pos.CENTER);
        getChildren().add(dateTimeLabel);
    }

    public void startUpdating() {
        timeline.play();
    }

    public void stopUpdating() {
        timeline.stop();
    }

    private String formatDateWithOrdinal(LocalDateTime dateTime) {
        int dayOfMonth = dateTime.getDayOfMonth();
        String ordinalIndicator;
        if (dayOfMonth >= 11 && dayOfMonth <= 13) {
            ordinalIndicator = "th";
        } else {
            int lastDigit = dayOfMonth % 10;
            switch (lastDigit) {
                case 1:
                    ordinalIndicator = "st";
                    break;
                case 2:
                    ordinalIndicator = "nd";
                    break;
                case 3:
                    ordinalIndicator = "rd";
                    break;
                default:
                    ordinalIndicator = "th";
                    break;
            }
        }
        return dateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d'" + ordinalIndicator + "' yyyy"));
    }

    private String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
    }
}
