package server;
import java.util.Date;

public class ChatikMessage
{
    private String command;
    private String message;
    private String sender;
    private Date received;

    public ChatikMessage() {}

    public ChatikMessage(String command, String message)
    {
        this.sender = "server";
        this.message = message;
        this.command = command;
        this.received = new Date();
    }

    public String getCommand() { return command; }

    public void setCommand(String command) { this.command = command; }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }
}
