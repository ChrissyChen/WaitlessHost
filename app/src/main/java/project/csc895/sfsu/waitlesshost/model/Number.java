package project.csc895.sfsu.waitlesshost.model;

/**
 * Created by Chrissy on 3/12/18.
 */

import java.io.Serializable;

public class Number implements Serializable {

    private String numberID;
    private String restaurantID;
    private String restaurantName;
    private String userID;
    private String username;
    private String phone;
    private String email;
    private String timeCreated;
    private String numberName; // based on tableSize, A1, B2...
    private int partyNumber; // tableSize TODO: 9/14/17
    private String status;   // waiting, dining, canceled, completed

    public Number() {
    }

    public Number(String numberID, String restaurantID, String restaurantName, String userID, String username, String phone, String email, String timeCreated, String numberName, int partyNumber, String status) {
        this.numberID = numberID;
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.userID = userID;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.timeCreated = timeCreated;
        this.numberName = numberName;
        this.partyNumber = partyNumber;
        this.status = status;
    }

    public String getNumberID() {
        return numberID;
    }

    public void setNumberID(String numberID) {
        this.numberID = numberID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getNumberName() {
        return numberName;
    }

    public void setNumberName(String numberName) {
        this.numberName = numberName;
    }

    public int getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(int partyNumber) {
        this.partyNumber = partyNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}