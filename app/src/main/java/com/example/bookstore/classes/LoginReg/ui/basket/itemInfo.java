package com.example.bookstore.classes.LoginReg.ui.basket;

public class itemInfo {
    private String name;
    private String price;
    private String image;
    private String quantity;

    public itemInfo() {
    }

    public itemInfo(String name, String price, String image, String quantity) {
        this.name = name;
        this.price = price;
        this.image = image;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


}
