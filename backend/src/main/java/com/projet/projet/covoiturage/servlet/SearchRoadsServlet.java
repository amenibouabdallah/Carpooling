package com.projet.projet.covoiturage.servlet;

import com.projet.projet.covoiturage.config.DatabaseConnection;
import com.projet.projet.covoiturage.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import com.google.gson.*;

@WebServlet("/searchRoads")
public class SearchRoadsServlet extends HttpServlet {

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

        // Ensure only passengers can search for roads
        if (!"passager".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Only passengers can search for roads.\"}");
            return;
        }

        // Retrieve search criteria from query parameters
        String departurePoint = request.getParameter("departurePoint");
        String dateTime = request.getParameter("dateTime");
        String price = request.getParameter("price");

        // Build dynamic SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT r.*, u.name AS driver_name FROM roads r INNER JOIN users u ON r.driver_id = u.id WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (departurePoint != null && !departurePoint.isEmpty()) {
            queryBuilder.append(" AND r.departure_point LIKE ?");
            parameters.add("%" + departurePoint + "%");
        }
        if (dateTime != null && !dateTime.isEmpty()) {
            queryBuilder.append(" AND r.date_time = ?");
            parameters.add(dateTime);
        }
        if (price != null && !price.isEmpty()) {
            queryBuilder.append(" AND r.price_per_place <= ?");
            parameters.add(Double.parseDouble(price));
        }

        // Execute the query
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

            // Set parameters dynamically
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            List<Map<String, Object>> roads = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> road = new HashMap<>();
                road.put("id", resultSet.getInt("id"));
                road.put("departurePoint", resultSet.getString("departure_point"));
                road.put("dateTime", resultSet.getString("date_time"));
                road.put("possiblePlaces", resultSet.getInt("possible_places"));
                road.put("pricePerPlace", resultSet.getDouble("price_per_place"));
                road.put("comment", resultSet.getString("comment"));
                road.put("driverName", resultSet.getString("driver_name"));
                roads.add(road);
            }

            // Respond with the list of roads as JSON
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new com.google.gson.Gson().toJson(roads));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        }
    }
}
