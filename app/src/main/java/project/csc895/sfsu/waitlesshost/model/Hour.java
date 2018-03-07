package project.csc895.sfsu.waitlesshost.model;

import java.util.ArrayList;

/**
 * Created by Chrissy on 3/6/18.
 */

public class Hour {

    private String hourID;
    private String restaurantID;
    private ArrayList<String> sunday, monday, tuesday, wednesday, thursday, friday, saturday;

    public Hour() {
    }

    public Hour(String hourID, String restaurantID, ArrayList<String> sunday, ArrayList<String> monday, ArrayList<String> tuesday, ArrayList<String> wednesday, ArrayList<String> thursday, ArrayList<String> friday, ArrayList<String> saturday) {
        this.hourID = hourID;
        this.restaurantID = restaurantID;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
    }

    public String getHourID() {
        return hourID;
    }

    public void setHourID(String hourID) {
        this.hourID = hourID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public ArrayList<String> getSunday() {
        return sunday;
    }

    public void setSunday(ArrayList<String> sunday) {
        this.sunday = sunday;
    }

    public ArrayList<String> getMonday() {
        return monday;
    }

    public void setMonday(ArrayList<String> monday) {
        this.monday = monday;
    }

    public ArrayList<String> getTuesday() {
        return tuesday;
    }

    public void setTuesday(ArrayList<String> tuesday) {
        this.tuesday = tuesday;
    }

    public ArrayList<String> getWednesday() {
        return wednesday;
    }

    public void setWednesday(ArrayList<String> wednesday) {
        this.wednesday = wednesday;
    }

    public ArrayList<String> getThursday() {
        return thursday;
    }

    public void setThursday(ArrayList<String> thursday) {
        this.thursday = thursday;
    }

    public ArrayList<String> getFriday() {
        return friday;
    }

    public void setFriday(ArrayList<String> friday) {
        this.friday = friday;
    }

    public ArrayList<String> getSaturday() {
        return saturday;
    }

    public void setSaturday(ArrayList<String> saturday) {
        this.saturday = saturday;
    }
}
