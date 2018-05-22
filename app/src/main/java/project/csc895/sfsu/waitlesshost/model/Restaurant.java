package project.csc895.sfsu.waitlesshost.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Restaurant{
    private String restaurantID;
    private String name;
    private String imageUrl;
    private String address;
    private String telephone;
    private String cuisine;
    //private List<String> dishes;
    private String managerID;  // User UID
    private String email;
    private String password;

    public Restaurant() {
    }

    public Restaurant(String restaurantID, String name, String imageUrl, String address, String telephone, String cuisine, String managerID, String email) {
        this.restaurantID = restaurantID;
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.telephone = telephone;
        this.cuisine = cuisine;
        this.managerID = managerID;
        this.email = email;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
