package ro.ubbcluj.map.thecoders.repository.file;

import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.Validator;

import java.io.IOException;
import java.util.List;

public class UserFile extends AbstractFileRepository<Long, User> {

    public UserFile(String fileName, String fileFriendshipName, Validator<User> validator) throws IOException {
        super(fileName,fileFriendshipName, validator);
    }

    /**
     *
     * @param attributes The list of attributes read from the file
     * @return A user
     */
    @Override
    public User extractEntity(List<String> attributes) {
        User us = new User(attributes.get(1),attributes.get(2));
        us.setId(Long.parseLong(attributes.get(0)));
        return us;
    }

    /**
     *
     * @param entity The entity to be written to file
     * @return The User as String
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId().toString() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }

    /**
     *
     * @param attributes The ids of the users
     */
    public void addFriendshipsFromFile(List<String> attributes) throws IOException {
        for (int i = 1; i < attributes.size(); i++) {
            super.addFriendRepo(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(i)));
        }
    }

    /**
     *
     * @param entity The entity of which friendships to be written to file
     * @return The friendship as a String else null if it could not be converted
     */
    @Override
    protected String createFriendshipsAsString(User entity) {
        StringBuilder friendships = new StringBuilder();
        friendships.append(entity.getId().toString());
        friendships.append(";");
        for(int i = 0; i < entity.getFriends().size(); i++){
             friendships.append(entity.getFriends().get(i).getId());
             friendships.append(";");
        }
        if(friendships.length() > 2) {
             friendships.deleteCharAt(friendships.length() - 1);
        }
        return friendships.toString();
    }


}

