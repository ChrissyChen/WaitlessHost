package project.csc895.sfsu.waitlesshost.model;

/**
 * Created by Chrissy on 3/31/18.
 */

public class Message {
    private String sender;    // store restaurant ID
    private String recipient; // store user registered device FCM token
    private String title;
    private String message;

    public Message() {
    }

    public Message(String sender, String recipient, String title, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.title = title;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
