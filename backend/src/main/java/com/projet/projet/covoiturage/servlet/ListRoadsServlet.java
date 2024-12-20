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

@WebServlet("/listRoads")
public class ListRoadsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // SQL Query to get road details
        String query = "SELECT id, departure_point, arrival_point FROM roads";

        // List to hold the roads data
        List<JSONObject> roadList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            // Process each row and add it to the list
            while (rs.next()) {
                JSONObject road = new JSONObject();
                road.put("id", rs.getInt("id"));
                road.put("departure_point", rs.getString("departure_point"));
                road.put("arrival_point", rs.getString("arrival_point"));
                roadList.add(road);
            }

            // Convert list to JSON array and return response
            JSONArray jsonResponse = new JSONArray(roadList);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(jsonResponse.toString());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred while fetching roads.\"}");
        }
    }
}
