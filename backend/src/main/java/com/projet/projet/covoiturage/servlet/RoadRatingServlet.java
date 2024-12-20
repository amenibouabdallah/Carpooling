package com.projet.projet.covoiturage.servlet;

import com.projet.projet.covoiturage.config.DatabaseConnection;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/roadRating")
public class RoadRatingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Get road_id from request parameter
        String roadIdParam = request.getParameter("road_id");
        if (roadIdParam == null || roadIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"road_id parameter is required.\"}");
            return;
        }

        int roadId;
        try {
            roadId = Integer.parseInt(roadIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid road_id format.\"}");
            return;
        }

        // Query to calculate average rating
        String query = "SELECT COALESCE(AVG(rating), 0) AS avg_rating FROM reviews WHERE road_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, roadId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double avgRating = rs.getDouble("avg_rating");

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(String.format(
                        "{\"status\": \"success\", \"road_id\": %d, \"average_rating\": %.2f}",
                        roadId, avgRating
                ));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Road not found or has no ratings.\"}");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred.\"}");
        }
    }
}
