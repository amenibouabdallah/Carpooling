package com.projet.projet.covoiturage.servlet;

import com.projet.projet.covoiturage.config.DatabaseConnection;
import com.projet.projet.covoiturage.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import io.jsonwebtoken.JwtException;

import java.sql.ResultSet;



@WebServlet("/addReview")
public class ReviewServlet extends HttpServlet {

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

        // Ensure only passengers can add reviews
        if (!"passager".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only passengers can add reviews.\"}");
            return;
        }

        // Retrieve review details
        int roadId = Integer.parseInt(request.getParameter("roadId"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        // Validate rating
        if (rating < 1 || rating > 5) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Rating must be between 1 and 5.\"}");
            return;
        }

        // Insert review into database
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO reviews (road_id, passenger_email, rating, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roadId);
            statement.setString(2, email);
            statement.setInt(3, rating);
            statement.setString(4, comment);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Review added successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Failed to add review.\"}");
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
        // Extract road ID from the query parameters
        String roadIdParam = request.getParameter("roadId");
        if (roadIdParam == null || roadIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Road ID is required.\"}");
            return;
        }

        int roadId;
        try {
            roadId = Integer.parseInt(roadIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid Road ID.\"}");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Query to fetch reviews for the given road ID
            String query = "SELECT r.rating, r.comment, r.created_at, u.name AS passenger_name " +
                    "FROM reviews r " +
                    "JOIN users u ON r.passenger_email = u.email " +
                    "WHERE r.road_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roadId);

            ResultSet rs = statement.executeQuery();

            // Build JSON response
            StringBuilder jsonResponse = new StringBuilder("{\"status\": \"success\", \"reviews\": [");
            boolean hasReviews = false;

            while (rs.next()) {
                hasReviews = true;
                if (jsonResponse.length() > 33) jsonResponse.append(",");
                jsonResponse.append("{")
                        .append("\"rating\":").append(rs.getInt("rating")).append(",")
                        .append("\"comment\":\"").append(rs.getString("comment")).append("\",")
                        .append("\"createdAt\":\"").append(rs.getTimestamp("created_at")).append("\",")
                        .append("\"passengerName\":\"").append(rs.getString("passenger_name")).append("\"")
                        .append("}");
            }

            jsonResponse.append("]}");

            if (!hasReviews) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"status\": \"success\", \"message\": \"No reviews found for this road.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(jsonResponse.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred while fetching reviews.\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
