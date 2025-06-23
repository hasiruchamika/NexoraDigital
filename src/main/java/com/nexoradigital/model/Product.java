package com.nexoradigital.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private String type; // Template or Service
    private double price;
    private String description;
    private String imageUrl;

    // Default constructor
    public Product() {}

    // Parameterized constructor
    public Product(int id, String name, String type, double price, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // toString for serialization
    @Override
    public String toString() {
        return id + "," + name + "," + type + "," + price + "," + description + "," + imageUrl;
    }
}
