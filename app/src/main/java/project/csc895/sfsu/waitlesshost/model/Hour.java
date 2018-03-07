package project.csc895.sfsu.waitlesshost.model;

import java.util.ArrayList;

/**
 * Created by Chrissy on 3/6/18.
 */

public class Hour {

    private String hourID;
    private String restaurantID;
    private ArrayList<String> Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;

    public Hour() {
    }

    public Hour(String hourID, String restaurantID, ArrayList<String> sunday, ArrayList<String> monday, ArrayList<String> tuesday, ArrayList<String> wednesday, ArrayList<String> thursday, ArrayList<String> friday, ArrayList<String> saturday) {
        this.hourID = hourID;
        this.restaurantID = restaurantID;
        this.Sunday = sunday;
        this.Monday = monday;
        this.Tuesday = tuesday;
        this.Wednesday = wednesday;
        this.Thursday = thursday;
        this.Friday = friday;
        this.Saturday = saturday;
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
        return Sunday;
    }

    public void setSunday(ArrayList<String> sunday) {
        Sunday = sunday;
    }

    public ArrayList<String> getMonday() {
        return Monday;
    }

    public void setMonday(ArrayList<String> monday) {
        Monday = monday;
    }

    public ArrayList<String> getTuesday() {
        return Tuesday;
    }

    public void setTuesday(ArrayList<String> tuesday) {
        Tuesday = tuesday;
    }

    public ArrayList<String> getWednesday() {
        return Wednesday;
    }

    public void setWednesday(ArrayList<String> wednesday) {
        Wednesday = wednesday;
    }

    public ArrayList<String> getThursday() {
        return Thursday;
    }

    public void setThursday(ArrayList<String> thursday) {
        Thursday = thursday;
    }

    public ArrayList<String> getFriday() {
        return Friday;
    }

    public void setFriday(ArrayList<String> friday) {
        Friday = friday;
    }

    public ArrayList<String> getSaturday() {
        return Saturday;
    }

    public void setSaturday(ArrayList<String> saturday) {
        Saturday = saturday;
    }
}
