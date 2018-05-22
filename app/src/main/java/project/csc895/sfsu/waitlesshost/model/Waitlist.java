package project.csc895.sfsu.waitlesshost.model;

public class Waitlist {

    private String waitlistID;
    private String restaurantID;
    private int waitNumTableA, waitNumTableB, waitNumTableC, waitNumTableD;
    private int counterTableA, counterTableB, counterTableC, counterTableD;

    public Waitlist() {
    }

    public Waitlist(String waitlistID, String restaurantID, int waitNumTableA, int waitNumTableB, int waitNumTableC, int waitNumTableD, int counterTableA, int counterTableB, int counterTableC, int counterTableD) {
        this.waitlistID = waitlistID;
        this.restaurantID = restaurantID;
        this.waitNumTableA = waitNumTableA;
        this.waitNumTableB = waitNumTableB;
        this.waitNumTableC = waitNumTableC;
        this.waitNumTableD = waitNumTableD;
        this.counterTableA = counterTableA;
        this.counterTableB = counterTableB;
        this.counterTableC = counterTableC;
        this.counterTableD = counterTableD;
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

    public int getCounterTableA() {
        return counterTableA;
    }

    public void setCounterTableA(int counterTableA) {
        this.counterTableA = counterTableA;
    }

    public int getCounterTableB() {
        return counterTableB;
    }

    public void setCounterTableB(int counterTableB) {
        this.counterTableB = counterTableB;
    }

    public int getCounterTableC() {
        return counterTableC;
    }

    public void setCounterTableC(int counterTableC) {
        this.counterTableC = counterTableC;
    }

    public int getCounterTableD() {
        return counterTableD;
    }

    public void setCounterTableD(int counterTableD) {
        this.counterTableD = counterTableD;
    }
}
