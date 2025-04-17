package com.example.hometouch2.FurnitureItem;

public class FurnitureItem {
    private String name;
    private String price;

    private String type;

    private String status;

    private int quantity;
    private int imageResId;
    private int selectedQuantity;


    // Constructor
    public FurnitureItem(String name, String price, int imageResId, int quantity, String type, String status) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        this.imageResId = imageResId;
        this.selectedQuantity = 1;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
