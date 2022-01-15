package ro.ubbcluj.map.thecoders.domain;

import java.util.Date;
import java.util.List;

public class Request extends Entity<Long> {
    private User user;
    private String firstName;
    private String lastName;
    private String userName;
    private String status;
    private Date date;
    private Long id;


    public Request(User user,String status, Date date){
        this.user = user;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.status = status;
        this.date = date;
        setId(user.getId());
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId(){ return id;}

    public void setId(Long id) { this.id = id;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}
}
