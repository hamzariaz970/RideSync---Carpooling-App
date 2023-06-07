package com.example.ridesync.Classes;

import java.sql.*;

public class Driver extends User{
    private Vehicle vehicle;

    public Driver(String name, int cmsID, String university, String address, Vehicle vehicle)
    {
        super(name, cmsID, university, address);
        this.vehicle = vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public static Vehicle getVehicleFromDatabase(int cmsID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync?useUnicode=true&characterEncoding=utf8", "root", "");
            preparedStatement = connection.prepareStatement("SELECT vehicle_model, vehicle_license, seats_available FROM drivers WHERE cms_id = ?");
            preparedStatement.setInt(1, cmsID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve vehicle details from the result set
                String model = resultSet.getString("vehicle_model");
                String licensePlate = resultSet.getString("vehicle_license");
                int seatsAvailable = resultSet.getInt("seats_available");

                // Create a Vehicle object with the retrieved details
                Vehicle vehicle = new Vehicle(model, licensePlate, seatsAvailable);
                return vehicle;
            }
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

        return null; // Return null if vehicle not found or if an exception occurs
    }



}
