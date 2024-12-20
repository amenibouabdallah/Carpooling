package com.projet.projet.covoiturage.servlet;

import com.projet.projet.covoiturage.config.DatabaseConnection;
import com.projet.projet.covoiturage.model.User;
import com.projet.projet.covoiturage.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/userProfile")
public class UserProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Extract token from the Authorization header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Token is missing or invalid.\"}");
            return;
        }

        JwtUtil jwtUtil = new JwtUtil();
        String email;
        try {
            // Extract the user's email from the token
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        // Retrieve user details from the database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT name, phoneNumber, email, address, isVerified, role FROM users WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Map user data to a User object
                User user = new User();
                user.setName(rs.getString("name"));
                user.setPhoneNumber(rs.getInt("phoneNumber"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setVerified(rs.getBoolean("isVerified"));
                user.setRole(rs.getString("role"));

                // Convert User object to JSON
                String userJson = String.format(
                        "{\"name\": \"%s\", \"phoneNumber\": %d, \"email\": \"%s\", \"address\": \"%s\", \"isVerified\": %b, \"role\": \"%s\"}",
                        user.getName(), user.getPhoneNumber(), user.getEmail(), user.getAddress(), user.isVerified(), user.getRole()
                );

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(userJson);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"User not found.\"}");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        }
    }
}
