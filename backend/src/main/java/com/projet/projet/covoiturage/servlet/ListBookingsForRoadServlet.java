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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/listBookings")
public class ListBookingsForRoadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Retrieve road_id from request parameters
        String roadIdParam = request.getParameter("road_id");
        if (roadIdParam == null || roadIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"road_id is required.\"}");
            return;
        }

        int roadId = Integer.parseInt(roadIdParam);

        // SQL Query to fetch bookings for the given road
        String query = """
            SELECT id, passenger_email, departure_point, arrival_point, price_per_place, comment, date_time
            FROM bookings
            WHERE road_id = ?
        """;

        JSONArray bookingsArray = new JSONArray();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, roadId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject booking = new JSONObject();
                booking.put("booking_id", rs.getInt("id"));
                booking.put("passenger_email", rs.getString("passenger_email"));
                booking.put("departure_point", rs.getString("departure_point"));
                booking.put("arrival_point", rs.getString("arrival_point"));
                booking.put("price_per_place", rs.getDouble("price_per_place"));
                booking.put("comment", rs.getString("comment"));
                booking.put("date_time", rs.getString("date_time"));
                bookingsArray.put(booking);
            }

            // Return the JSON response
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(bookingsArray.toString());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred while fetching bookings.\"}");
        }
    }
}
