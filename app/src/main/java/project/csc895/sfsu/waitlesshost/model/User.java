package project.csc895.sfsu.waitlesshost.model;


import java.util.ArrayList;
import java.util.List;

public class User {
    private String userID;
    private String firstName;
    private String lastName;
    private String telephone;
    private String email;
    private String password;  // didn't store in db
    private String photoUrl;  // TODO
    private List<String> favorites; // store restaurant_ids TODO
    private String tokenFCM; // FCM registered InstanceID token

    public User() {
    }

    public User(String userID, String firstName, String lastName, String telephone, String email, String tokenFCM) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
        this.tokenFCM = tokenFCM;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public String getTokenFCM() {
        return tokenFCM;
    }

    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }
}
