package project.csc895.sfsu.waitlesshost.model;

public class Table {
    private String tableID;
    private String restaurantID;
    //private String orderID;
    private int tableSize;
    private String tableName;
    private String status;    // open, seated, dirty
    private String numberID;  // NULL if table status is open or dirty. Only has value if table status is seated

    public Table() {
    }

    public Table(String tableID, String restaurantID, int tableSize, String tableName, String status, String numberID) {
        this.tableID = tableID;
        this.restaurantID = restaurantID;
        this.tableSize = tableSize;
        this.tableName = tableName;
        this.status = status;
        this.numberID = numberID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumberID() {
        return numberID;
    }

    public void setNumberID(String numberID) {
        this.numberID = numberID;
    }
}
