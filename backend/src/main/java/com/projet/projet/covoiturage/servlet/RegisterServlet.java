package com.projet.projet.covoiturage.servlet;

import com.projet.projet.covoiturage.model.User;
import com.projet.projet.covoiturage.config.DatabaseConnection;
import com.projet.projet.covoiturage.util.EmailUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user input
        String name = request.getParameter("name");
        int phoneNumber = Integer.parseInt(request.getParameter("phoneNumber"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String role = request.getParameter("role");

        // Generate a verification code
        String verificationCode = UUID.randomUUID().toString();

        // Store user details in the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query;
            query = "INSERT INTO users (name, phoneNumber, email, password, address, isVerified, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, phoneNumber);
            statement.setString(3, email);
            statement.setString(4, password); // Ensure password is hashed in production
            statement.setString(5, address);
            statement.setBoolean(6, false); // Initially unverified
            statement.setString(7, role);
            statement.executeUpdate();

            // Send verification email

            // Respond with success message
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\": \"success\", \"message\": \"User registered successfully. Verification email sent.\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to register user.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
