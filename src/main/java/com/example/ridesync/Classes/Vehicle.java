package com.example.ridesync.Classes;

public class Vehicle {

    private String model, licensePlate;
    private int seatsAvailable;

    public Vehicle(String model, String licensePlate, int seatsAvailable)
    {
        this.model = model;
        this.licensePlate = licensePlate;
        this.seatsAvailable = seatsAvailable;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }
}
