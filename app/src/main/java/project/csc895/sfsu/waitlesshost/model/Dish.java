package project.csc895.sfsu.waitlesshost.model;

public class Dish {
    private String dishID;
    private String name;
    private double price;
    private String restaurantID;
//    private String description;
//    private String imageUrl;

    public Dish(){

    }

    public Dish(String dishID, String name, double price, String restaurantID) {
        this.dishID = dishID;
        this.name = name;
        this.price = price;
        this.restaurantID = restaurantID;
    }

    public String getDishID() {
        return dishID;
    }

    public void setDishID(String dishID) {
        this.dishID = dishID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
}
