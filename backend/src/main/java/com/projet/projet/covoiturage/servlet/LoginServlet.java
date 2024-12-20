package com.projet.projet.covoiturage.servlet;

import com.projet.projet.covoiturage.util.JwtUtil;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import com.projet.projet.covoiturage.config.DatabaseConnection;
import io.jsonwebtoken.*;
import java.util.Date;

import static io.jsonwebtoken.Jwts.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve login credentials from request
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate user credentials
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";  // Make sure the password is hashed in production
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password); // Ensure password is hashed in production

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // User exists and credentials match
                String role = resultSet.getString("role");
                boolean isVerified = resultSet.getBoolean("isVerified");

                // We assume email verification is manually done, skip verification check here

                // Generate JWT token with email and role included
                JwtUtil jwtUtil = new JwtUtil();
                String token = jwtUtil.generateToken(email, role);

                // Extract role and email from the token (using the methods you implemented)
                String extractedRole = jwtUtil.extractRole(token);
                String extractedEmail = jwtUtil.extractEmail(token);

                // Prepare response JSON with token, role, and email
                JsonObject jsonResponse = Json.createObjectBuilder()
                        .add("status", "success")
                        .add("token", token)
                        .add("role", extractedRole)
                        .add("email", extractedEmail)
                        .build();
                System.out.println("Generated Token: " + token);
                System.out.println("Extracting Role from Token: " + extractedRole);

                // Set response status and send the response JSON
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(jsonResponse.toString());

            } else {
                // Invalid credentials
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid email or password.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred while processing your request.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
