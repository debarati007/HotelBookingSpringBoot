package com.booking.recruitment.hotel.model;

public class Ranking {
    private Hotel hotel;
    private Double distance;

    @Override
    public String toString() {
        return "Ranking{" +
                "hotel=" + hotel +
                ", distance=" + distance +
                '}';
    }

    public Ranking(Hotel hotel, Double distance) {
        this.hotel = hotel;
        this.distance = distance;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
