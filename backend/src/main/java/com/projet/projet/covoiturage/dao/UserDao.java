package com.projet.projet.covoiturage.dao;

import com.projet.projet.covoiturage.model.User;
import com.projet.projet.covoiturage.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private Connection connection;

    public UserDao() throws SQLException, ClassNotFoundException {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean isEmailUnique(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) == 0;
    }

    public void saveUser(User user) throws SQLException {
        String query = "INSERT INTO users (name, phoneNumber, email, password, address, isVerified, role) VALUES (?, ?, ?, ?, ?, 1, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.getName());
        ps.setInt(2, user.getPhoneNumber());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getAddress());
        ps.setString(6, user.getRole());
        ps.executeUpdate();
    }
}
