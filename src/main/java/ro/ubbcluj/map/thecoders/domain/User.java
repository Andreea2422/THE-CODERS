package ro.ubbcluj.map.thecoders.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Date date;
    private List<User> friends;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new ArrayList<>();
    }

    public User(String firstName, String lastName, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        friends = new ArrayList<>();
    }


    /**
     * Getter for the FirstName of a USer
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName The String to be set as the firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for the LastName of a User
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName The String to be set as the lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    /**
     * Getter for the list of friends
     * @return the list of friends
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * If the user already has the given user as a friend it will not be added
     * @param friend The user to be added as a friend
     */
    public void addFriend(User friend){
        if(!friends.contains(friend)) {
            friends.add(friend);
            friend.getFriends().add(this);
        }
    }

    /**
     * Delete a friend of a user by the given id
     * @param id the id given to identify the friend
     */
    public void deleteFriend(Long id){
        boolean found = false;
        int i = 0;
        while(!found){
            if(friends.get(i).getId().equals(id)){
                friends.get(i).getFriends().remove(this);
                friends.remove(i);
                found = true;
            }
            i++;
        }
    }

    public void sendMessage(Message msg){

    }

    /**
     * Setter for the list of friends
     * @param friends the list to be set as friends
     */
    public void setFriends(List<User> friends){
        this.friends = friends;
    }

    /**
     * Printer for the User and the list of friends
     */
    public void print(){
        System.out.println("User: " + firstName + " " + lastName);
        System.out.println("-- Friend list --");
        for (User friend : friends) {
            System.out.println("    User: " + friend.getId()+ " " + friend.getFirstName() + " " + friend.getLastName());
        }
    }


    /**
     *
     * @param o A User
     * @return true if the objects are the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    /**
     *
     * @return the HashCode of a User
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
