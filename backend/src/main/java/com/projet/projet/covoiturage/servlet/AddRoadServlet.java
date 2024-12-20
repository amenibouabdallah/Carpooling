/**package com.projet.projet.covoiturage.servlet;
import com.projet.projet.covoiturage.config.DatabaseConnection;
import com.projet.projet.covoiturage.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;



@WebServlet("/addRoad")
public class AddRoadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extract token from the Authorization header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove 'Bearer ' prefix
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Token is missing or invalid.\"}");
            return;
        }

        JwtUtil jwtUtil = new JwtUtil();
        String role;
        try {
            // Validate and extract the role from the token
            role = jwtUtil.extractRole(token);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Token expired.\"}");
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        // Ensure only drivers can add roads
        if (!"driver".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only drivers can add roads.\"}");
            return;
        }

        // Retrieve road details
        String driverEmail = jwtUtil.extractEmail(token);
        String departurePoint = request.getParameter("departurePoint");
        String arrivalPoint = request.getParameter("arrivalPoint");
        String dateTime = request.getParameter("dateTime");
        int possiblePlaces = Integer.parseInt(request.getParameter("possiblePlaces"));
        double pricePerPlace = Double.parseDouble(request.getParameter("pricePerPlace"));
        String comment = request.getParameter("comment");

        // Insert road into database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = """
                INSERT INTO roads 
                (driver_id, departure_point, arrival_point, date_time, possible_places, price_per_place, comment) 
                SELECT id, ?, ?, ?, ?, ?, ? FROM users WHERE email = ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, departurePoint);
            statement.setString(2, arrivalPoint);
            statement.setString(3, dateTime);
            statement.setInt(4, possiblePlaces);
            statement.setDouble(5, pricePerPlace);
            statement.setString(6, comment);
            statement.setString(7, driverEmail);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Road added successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to add road.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}**/

package com.projet.projet.covoiturage.servlet;

import com.projet.projet.covoiturage.config.DatabaseConnection;
import com.projet.projet.covoiturage.util.EmailUtil;
import com.projet.projet.covoiturage.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@WebServlet("/addRoad")
public class AddRoadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extract token from the Authorization header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove 'Bearer ' prefix
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Token is missing or invalid.\"}");
            return;
        }

        JwtUtil jwtUtil = new JwtUtil();
        String role;
        try {
            // Validate and extract the role from the token
            role = jwtUtil.extractRole(token);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Token expired.\"}");
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        // Ensure only drivers can add roads
        if (!"driver".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only drivers can add roads.\"}");
            return;
        }

        // Retrieve road details
        String driverEmail = jwtUtil.extractEmail(token);
        String departurePoint = request.getParameter("departurePoint");
        String arrivalPoint = request.getParameter("arrivalPoint");
        String dateTime = request.getParameter("dateTime");
        int possiblePlaces = Integer.parseInt(request.getParameter("possiblePlaces"));
        double pricePerPlace = Double.parseDouble(request.getParameter("pricePerPlace"));
        String comment = request.getParameter("comment");

        // Insert road into database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = """
                INSERT INTO roads 
                (driver_id, departure_point, arrival_point, date_time, possible_places, price_per_place, comment) 
                SELECT id, ?, ?, ?, ?, ?, ? FROM users WHERE email = ?
            """;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, departurePoint);
            statement.setString(2, arrivalPoint);
            statement.setString(3, dateTime);
            statement.setInt(4, possiblePlaces);
            statement.setDouble(5, pricePerPlace);
            statement.setString(6, comment);
            statement.setString(7, driverEmail);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Send email notifications to all users
                sendEmailNotifications(connection, departurePoint, arrivalPoint, dateTime, pricePerPlace, comment);

                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Road added successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to add road.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmailNotifications(Connection connection, String departurePoint, String arrivalPoint, String dateTime, double pricePerPlace, String comment) {
        try {
            // Fetch all user emails
            String userQuery = "SELECT email FROM users WHERE role = 'passager'";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            ResultSet resultSet = userStatement.executeQuery();

            List<String> emails = new ArrayList<>();
            while (resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }

            // Prepare email content
            String subject = "New Road Added!";
            String body = String.format("""
                A new road has been added:
                - Departure: %s
                - Arrival: %s
                - Date & Time: %s
                - Price per Place: %.2f
                - Comment: %s
                
                Book your spot now!
            """, departurePoint, arrivalPoint, dateTime, pricePerPlace, comment);

            // Send email to all users
            for (String email : emails) {
                EmailUtil.sendEmail(email, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



