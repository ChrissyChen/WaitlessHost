package project.csc895.sfsu.waitlesshost.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Restaurant implements Serializable {
    private String restaurantID;
    private String name;
    private String imageUrl;
    private String address;
//    private String streetAddress;
//    private String city;
//    private String state;
//    private String zip;
    private String telephone;
    private Map<String, Date> openTime; // TODO: 9/14/17
    //private List<String> cuisineTags;
    private String cuisine;
    private List<String> dishes;
    private String managerID;  // User UID
    private String email;
    private String password;
    private Map<Integer, List<String>> tables; //tableSize: tableID //// TODO: 9/14/17

    public Restaurant() {
    }

    public Restaurant(String restaurantID, String name, String cuisine, String address, String telephone, String managerID, String email) {
        this.restaurantID = restaurantID;
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
        this.telephone = telephone;
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

//    public String getStreetAddress() {
//        return streetAddress;
//    }
//
//    public void setStreetAddress(String streetAddress) {
//        this.streetAddress = streetAddress;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public String getZip() {
//        return zip;
//    }
//
//    public void setZip(String zip) {
//        this.zip = zip;
//    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Map<String, Date> getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Map<String, Date> openTime) {
        this.openTime = openTime;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

//    public List<String> getCuisineTags() {
//        return cuisineTags;
//    }
//
//    public String getCuisineTagsString() {
//        int len = getCuisineTags().size();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < len - 1; i++) {
//            sb.append(getCuisineTags().get(i));
//            sb.append(", ");
//        }
//        sb.append(getCuisineTags().get(len-1));
//        return sb.toString();
//    }
//
//    public void setCuisineTags(List<String> cuisineTags) {
//        this.cuisineTags = cuisineTags;
//    }

    public List<String> getDishes() {
        return dishes;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
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

    public Map<Integer, List<String>> getTables() {
        return tables;
    }

    public void setTables(Map<Integer, List<String>> tables) {
        this.tables = tables;
    }
}
