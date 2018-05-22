package project.csc895.sfsu.waitlesshost.model;

import java.util.Date;
import java.util.Map;

public class Order {
    private String orderID;
    private String userID;
    private String restaurantID;
    private Map<String, Integer> oderDetails; //dishID: quantity
    private double totalCost;
    private Date createdDate;

    public Order() {
    }

    public Order(String orderID, String userID, String restaurantID,
                 Map<String, Integer> oderDetails, double totalCost,
                 Date createdDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.restaurantID = restaurantID;
        this.oderDetails = oderDetails;
        this.totalCost = totalCost;
        this.createdDate = createdDate;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Map<String, Integer> getOderDetails() {
        return oderDetails;
    }

    public void setOderDetails(Map<String, Integer> oderDetails) {
        this.oderDetails = oderDetails;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
