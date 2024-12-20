package com.projet.projet.covoiturage.model;

import java.time.LocalDateTime;

public class Road {
    private int id;
    private int driverId;
    private String departurePoint;
    private LocalDateTime dateTime;
    private int possiblePlaces;
    private double pricePerPlace;
    private String comment;
    private String arrivalPoint;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getPossiblePlaces() {
        return possiblePlaces;
    }

    public void setPossiblePlaces(int possiblePlaces) {
        this.possiblePlaces = possiblePlaces;
    }

    public double getPricePerPlace() {
        return pricePerPlace;
    }

    public void setPricePerPlace(double pricePerPlace) {
        this.pricePerPlace = pricePerPlace;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
