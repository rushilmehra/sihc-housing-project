package com.scu.housing.other;

import java.util.ArrayList;

public class House {
    private int id;
    private String address;
    private boolean available;
    private double bath;
    private int bed;
    private String description;
    private double price;
    private double latitude;
    private double longitude;

    public House(int id, String address, boolean available, double bath, int bed, String description, double price, double latitude, double longitude) {
        this.id = id;
        this.address = address;
        this.available = available;
        this.bath = bath;
        this.bed = bed;
        this.description = description;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getBath() {
        return bath;
    }

    public void setBath(double bath) {
        this.bath = bath;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "I'm a house! I have these properties. ID: " + id + " Available: " + available + " Bath: " + bath + " Bed: " + bed + " Price: " + price + " Latitude: " + latitude + " Longitude: " + longitude;
    }

    public static ArrayList<House> getDummyData() {
        ArrayList<House> houses = new ArrayList<>();
        houses.add(new House(1, "2750 Wallace St, Santa Clara, CA 95051", true, 1.5, 1, "Description", 1000, 37.347155, -121.976423)); // Geocoding succeeds.
        houses.add(new House(2, "2951 Benton St, Santa Clara, CA 95051", true, 3, 2, "Description", 1500, 37.346774, -121.981034)); // Geocoding succeeds.
        houses.add(new House(3, "1350 Via Dondera, Santa Clara, CA 95051", false, 1, 2, "Description", 1250, 37.349887, -121.984660)); // Geocoding fails, uses coordinates.
        houses.add(new House(4, "3031 Orthello Way, Santa Clara, CA 95051", true, 2, 1, "Description", 1750, 37.348284, -121.980851)); // Geocoding succeeds.
        houses.add(new House(5, "1201 Morton Ave, Santa Clara, CA 95051", true, 1, 1, "Description", 500, 37.348056, -121.975454)); // Geocoding succeeds.
        houses.add(new House(5, "1350 Via Dondera, Santa Clara, CA 95051", true, 1, 1, "Description", 500, 0, 0)); // Geocoding fails, no coordinates provided! No pin displayed!
        return houses;
    }
}