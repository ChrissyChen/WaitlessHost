package project.csc895.sfsu.waitlesshost.model;

public class Table {
    private String tableID;
    private String restaurantID;
    //private String orderID;
    private int tableSize;
    private String tableName;
    private boolean isOccupied;
    private String userID;

    public Table() {
    }

    public Table(String tableID, String restaurantID, int tableSize, String tableName, boolean isOccupied, String userID) {
        this.tableID = tableID;
        this.restaurantID = restaurantID;
        this.tableSize = tableSize;
        this.tableName = tableName;
        this.isOccupied = isOccupied;
        this.userID = userID;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
