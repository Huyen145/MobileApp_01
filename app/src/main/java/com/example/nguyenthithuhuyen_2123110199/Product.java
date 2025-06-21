package com.example.nguyenthithuhuyen_2123110199;

public class Product {
    private String name;
    private String sellPrice;
    private String rentPrice;
    private int imageResId;

    public Product(String name, String sellPrice, String rentPrice, int imageResId) {
        this.name = name;
        this.sellPrice = sellPrice;
        this.rentPrice = rentPrice;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public int getImageResId() {
        return imageResId;
    }
}
