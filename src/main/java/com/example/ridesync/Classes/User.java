package com.example.ridesync.Classes;

import java.sql.*;

public class User extends Person{
    private int cmsID, balance;
    private String university;
    private String address;

    public User(String name, int cmsID, String university, String address)
    {
        super(name);
        this.cmsID = cmsID;
        this.university = university;
        this.address = address;
        this.balance = fetchBalanceFromDatabase(cmsID);
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCmsID(int cmsID) {
        this.cmsID = cmsID;
    }

    public int getCmsID() {
        return cmsID;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getUniversity() {
        return university;
    }

    public int fetchBalanceFromDatabase(int cmsID) {
        int balance = 0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/RideSync?useUnicode=true&characterEncoding=utf8", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT wallet_balance FROM RideSync.users WHERE cms_id = ?")) {
            preparedStatement.setInt(1, cmsID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getInt("wallet_balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    public int getBalance() {
        return balance;
    }
}



