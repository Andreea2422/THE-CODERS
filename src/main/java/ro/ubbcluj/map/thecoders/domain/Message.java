package ro.ubbcluj.map.thecoders.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Message {
    Long id;
    User from;
    List<User> to;
    String message;
    Date data;
    Message reply;

    public Message(Long id, User from, List<User> to, String message, Date data) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        reply = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }
}
