package project.csc895.sfsu.waitlesshost.model;

import java.util.ArrayList;

/**
 * Created by Chrissy on 3/7/18.
 */

public class RestaurantTable {
    private String restaurantTableID;
    private String restaurantID;
    private int numTableA, numTableB, numTableC, numTableD;
    private ArrayList<String> listTableA, listTableB, listTableC, listTableD;  // store table ids

    public RestaurantTable() {
    }

    public RestaurantTable(String restaurantTableID, String restaurantID, int numTableA, int numTableB, int numTableC, int numTableD) {
        this.restaurantTableID = restaurantTableID;
        this.restaurantID = restaurantID;
        this.numTableA = numTableA;
        this.numTableB = numTableB;
        this.numTableC = numTableC;
        this.numTableD = numTableD;
    }

    public RestaurantTable(String restaurantTableID, String restaurantID, int numTableA, int numTableB, int numTableC, int numTableD, ArrayList<String> listTableA, ArrayList<String> listTableB, ArrayList<String> listTableC, ArrayList<String> listTableD) {
        this.restaurantTableID = restaurantTableID;
        this.restaurantID = restaurantID;
        this.numTableA = numTableA;
        this.numTableB = numTableB;
        this.numTableC = numTableC;
        this.numTableD = numTableD;
        this.listTableA = listTableA;
        this.listTableB = listTableB;
        this.listTableC = listTableC;
        this.listTableD = listTableD;
    }

    public String getRestaurantTableID() {
        return restaurantTableID;
    }

    public void setRestaurantTableID(String restaurantTableID) {
        this.restaurantTableID = restaurantTableID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getNumTableA() {
        return numTableA;
    }

    public void setNumTableA(int numTableA) {
        this.numTableA = numTableA;
    }

    public int getNumTableB() {
        return numTableB;
    }

    public void setNumTableB(int numTableB) {
        this.numTableB = numTableB;
    }

    public int getNumTableC() {
        return numTableC;
    }

    public void setNumTableC(int numTableC) {
        this.numTableC = numTableC;
    }

    public int getNumTableD() {
        return numTableD;
    }

    public void setNumTableD(int numTableD) {
        this.numTableD = numTableD;
    }

    public ArrayList<String> getListTableA() {
        return listTableA;
    }

    public void setListTableA(ArrayList<String> listTableA) {
        this.listTableA = listTableA;
    }

    public ArrayList<String> getListTableB() {
        return listTableB;
    }

    public void setListTableB(ArrayList<String> listTableB) {
        this.listTableB = listTableB;
    }

    public ArrayList<String> getListTableC() {
        return listTableC;
    }

    public void setListTableC(ArrayList<String> listTableC) {
        this.listTableC = listTableC;
    }

    public ArrayList<String> getListTableD() {
        return listTableD;
    }

    public void setListTableD(ArrayList<String> listTableD) {
        this.listTableD = listTableD;
    }
}
