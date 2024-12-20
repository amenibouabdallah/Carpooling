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
import io.jsonwebtoken.JwtException;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    /**@Override
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
        String email;
        try {
            // Validate and extract details from the token
            role = jwtUtil.extractRole(token);
            email = jwtUtil.extractEmail(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        // Ensure only passengers can book
        if (!"passager".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only passengers can book.\"}");
            return;
        }

        // Retrieve booking details
        int roadId = Integer.parseInt(request.getParameter("roadId"));

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if road has available places
            String checkQuery = "SELECT * FROM roads WHERE id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, roadId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int availablePlaces = rs.getInt("possible_places");
                if (availablePlaces <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"status\": \"error\", \"message\": \"No available places on this road.\"}");
                    return;
                }

                // Retrieve road details
                String departurePoint = rs.getString("departure_point");
                String arrivalPoint = rs.getString("arrival_point");
                String dateTime = rs.getString("date_time");
                double pricePerPlace = rs.getDouble("price_per_place");
                String comment = rs.getString("comment");

                // Book the road with all road details
                String bookQuery = """
                    INSERT INTO bookings 
                    (road_id, passenger_email, departure_point, arrival_point, date_time, price_per_place, comment) 
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
                PreparedStatement bookStmt = connection.prepareStatement(bookQuery);
                bookStmt.setInt(1, roadId);
                bookStmt.setString(2, email);
                bookStmt.setString(3, departurePoint);
                bookStmt.setString(4, arrivalPoint);
                bookStmt.setString(5, dateTime);
                bookStmt.setDouble(6, pricePerPlace);
                bookStmt.setString(7, comment);
                bookStmt.executeUpdate();

                // Update available places
                String updateQuery = "UPDATE roads SET possible_places = possible_places - 1 WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, roadId);
                updateStmt.executeUpdate();

                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Booking successful.\"}");

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Road not found.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String email;
        try {
            // Validate and extract details from the token
            role = jwtUtil.extractRole(token);
            email = jwtUtil.extractEmail(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        // Ensure only passengers can cancel
        if (!"passager".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only passengers can cancel bookings.\"}");
            return;
        }

        // Retrieve booking details
        int roadId = Integer.parseInt(request.getParameter("roadId"));

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if the booking exists for the passenger
            String checkQuery = "SELECT id FROM bookings WHERE road_id = ? AND passenger_email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, roadId);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Delete the booking
                String deleteQuery = "DELETE FROM bookings WHERE road_id = ? AND passenger_email = ?";
                PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
                deleteStmt.setInt(1, roadId);
                deleteStmt.setString(2, email);
                deleteStmt.executeUpdate();

                // Update available places
                String updateQuery = "UPDATE roads SET possible_places = possible_places + 1 WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, roadId);
                updateStmt.executeUpdate();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Booking canceled successfully.\"}");

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Booking not found.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }**/
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
        String email;
        try {
            // Validate and extract details from the token
            role = jwtUtil.extractRole(token);
            email = jwtUtil.extractEmail(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        // Ensure only passengers can book
        if (!"passager".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only passengers can book.\"}");
            return;
        }

        // Retrieve booking details
        int roadId = Integer.parseInt(request.getParameter("roadId"));

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if road has available places
            String checkQuery = "SELECT * FROM roads WHERE id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, roadId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int availablePlaces = rs.getInt("possible_places");
                if (availablePlaces <= 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"status\": \"error\", \"message\": \"No available places on this road.\"}");
                    return;
                }

                // Retrieve road details
                String departurePoint = rs.getString("departure_point");
                String arrivalPoint = rs.getString("arrival_point");
                String dateTime = rs.getString("date_time");
                double pricePerPlace = rs.getDouble("price_per_place");
                String comment = rs.getString("comment");

                // Book the road with all road details
                String bookQuery = """
                    INSERT INTO bookings 
                    (road_id, passenger_email, departure_point, arrival_point, date_time, price_per_place, comment) 
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
                PreparedStatement bookStmt = connection.prepareStatement(bookQuery);
                bookStmt.setInt(1, roadId);
                bookStmt.setString(2, email);
                bookStmt.setString(3, departurePoint);
                bookStmt.setString(4, arrivalPoint);
                bookStmt.setString(5, dateTime);
                bookStmt.setDouble(6, pricePerPlace);
                bookStmt.setString(7, comment);
                bookStmt.executeUpdate();

                // Update available places
                String updateQuery = "UPDATE roads SET possible_places = possible_places - 1 WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, roadId);
                updateStmt.executeUpdate();

                // Send confirmation email
                String subject = "Booking Confirmation";
                String message = String.format("""
                        Dear User,
                        
                        Your booking was successful! Here are the details:
                        Departure: %s
                        Arrival: %s
                        Date & Time: %s
                        Price: %.2f
                        Comment: %s
                        
                        Thank you for choosing our service!
                        
                        Regards,
                        Your Covoiturage Team
                        """, departurePoint, arrivalPoint, dateTime, pricePerPlace, comment != null ? comment : "N/A");
                EmailUtil.sendEmail(email, subject, message);

                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Booking successful.\"}");

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Road not found.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Similar email notification logic for cancellation
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Token is missing or invalid.\"}");
            return;
        }

        JwtUtil jwtUtil = new JwtUtil();
        String role;
        String email;
        try {
            role = jwtUtil.extractRole(token);
            email = jwtUtil.extractEmail(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        if (!"passager".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only passengers can cancel bookings.\"}");
            return;
        }

        int roadId = Integer.parseInt(request.getParameter("roadId"));

        try (Connection connection = DatabaseConnection.getConnection()) {
            String checkQuery = "SELECT id FROM bookings WHERE road_id = ? AND passenger_email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, roadId);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String deleteQuery = "DELETE FROM bookings WHERE road_id = ? AND passenger_email = ?";
                PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
                deleteStmt.setInt(1, roadId);
                deleteStmt.setString(2, email);
                deleteStmt.executeUpdate();

                String updateQuery = "UPDATE roads SET possible_places = possible_places + 1 WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, roadId);
                updateStmt.executeUpdate();

                // Send cancellation email
                String subject = "Booking Cancellation";
                String message = String.format("""
                        Dear User,
                        
                        Your booking for road ID %d has been successfully canceled.
                        
                        Thank you for using our service.
                        
                        Regards,
                        Your Covoiturage Team
                        """, roadId);
                EmailUtil.sendEmail(email, subject, message);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Booking canceled successfully.\"}");

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Booking not found.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String email;
        try {
            // Validate and extract details from the token
            role = jwtUtil.extractRole(token);
            email = jwtUtil.extractEmail(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid token.\"}");
            return;
        }

        // Ensure only passengers can check booking history
        if (!"passager".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only passengers can view booking history.\"}");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Fetch booking history for the passenger
            String query = """
            SELECT b.id AS booking_id, 
                   b.departure_point, 
                   b.arrival_point, 
                   b.date_time, 
                   b.price_per_place, 
                   b.comment 
            FROM bookings b 
            WHERE b.passenger_email = ?
        """;
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // Build JSON response
            StringBuilder jsonResponse = new StringBuilder("{\"status\": \"success\", \"bookings\": [");
            boolean hasBookings = false;

            while (rs.next()) {
                hasBookings = true;
                if (jsonResponse.length() > 34) jsonResponse.append(",");
                jsonResponse.append("{")
                        .append("\"bookingId\":").append(rs.getInt("booking_id")).append(",")
                        .append("\"departurePoint\":\"").append(rs.getString("departure_point")).append("\",")
                        .append("\"arrivalPoint\":\"").append(rs.getString("arrival_point")).append("\",")
                        .append("\"departureTime\":\"").append(rs.getString("date_time")).append("\",")
                        .append("\"pricePerPlace\":").append(rs.getDouble("price_per_place")).append(",")
                        .append("\"comment\":\"").append(rs.getString("comment") != null ? rs.getString("comment") : "").append("\"")
                        .append("}");
            }

            jsonResponse.append("]}");

            if (!hasBookings) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"No bookings found.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(jsonResponse.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred while fetching booking history.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}