package com.example.bookstore.classes.LoginReg.ui.store;

public class book_info {
    private String name;
    private String price;
    private String image;
    private String details;
    private String availability;
    private String quantity;


    public book_info() {
    }

    public book_info(String name,String details, String price, String image, String availability, String quantity) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.image = image;
        this.availability = availability;
        this.quantity = quantity;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
