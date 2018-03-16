package project.csc895.sfsu.waitlesshost.model;

public class Waitlist {

    private String waitlistID;
    private String restaurantID;
    private int waitNumTableA, waitNumTableB, waitNumTableC, waitNumTableD;

    public Waitlist() {
    }

    public Waitlist(String waitlistID, String restaurantID, int waitNumTableA, int waitNumTableB, int waitNumTableC, int waitNumTableD) {
        this.waitlistID = waitlistID;
        this.restaurantID = restaurantID;
        this.waitNumTableA = waitNumTableA;
        this.waitNumTableB = waitNumTableB;
        this.waitNumTableC = waitNumTableC;
        this.waitNumTableD = waitNumTableD;
    }

    public String getWaitlistID() {
        return waitlistID;
    }

    public void setWaitlistID(String waitlistID) {
        this.waitlistID = waitlistID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getWaitNumTableA() {
        return waitNumTableA;
    }

    public void setWaitNumTableA(int waitNumTableA) {
        this.waitNumTableA = waitNumTableA;
    }

    public int getWaitNumTableB() {
        return waitNumTableB;
    }

    public void setWaitNumTableB(int waitNumTableB) {
        this.waitNumTableB = waitNumTableB;
    }

    public int getWaitNumTableC() {
        return waitNumTableC;
    }

    public void setWaitNumTableC(int waitNumTableC) {
        this.waitNumTableC = waitNumTableC;
    }

    public int getWaitNumTableD() {
        return waitNumTableD;
    }

    public void setWaitNumTableD(int waitNumTableD) {
        this.waitNumTableD = waitNumTableD;
    }
}


