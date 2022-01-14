package ro.ubbcluj.map.thecoders.repository.db;

import ro.ubbcluj.map.thecoders.domain.Entity;
import ro.ubbcluj.map.thecoders.domain.Message;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.Validator;
import ro.ubbcluj.map.thecoders.repository.paging.Page;
import ro.ubbcluj.map.thecoders.repository.paging.Pageable;
import ro.ubbcluj.map.thecoders.repository.paging.PagingRepository;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class UtilizatorDbRepository<ID,E extends Entity<ID>> implements PagingRepository<ID, E> {
    private String url;
    private String username;
    private String password;
    private Validator<E> validator;
    private Map<ID,E> entities;
    private Map<Long,Message> messages;

    public UtilizatorDbRepository(String url, String username, String password, Validator<E> validator) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        entities = new HashMap<>();
        messages = new HashMap<>();
        load();
        loadFriends();
        loadMessages();
    }

    /**
     * Throws RepoException if the id is null
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return The entity with the specified id
     */
    @Override
    public E findOne(ID id) {
        String sql = "SELECT * FROM users WHERE id = " + id;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("user_name");
                String password = resultSet.getString("password");

                User utilizator = new User(firstName, lastName, username, password);
                utilizator.setId(resultSet.getLong("id"));
                return entities.get(utilizator.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that loads all users
     * @throws SQLException
     */
    private void load() throws SQLException{
        String sql = "SELECT * FROM users";
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("user_name");
            String password = resultSet.getString("password");

            User utilizator = new User(firstName, lastName, username, password);
            utilizator.setId(id);
            entities.put((ID) id, (E) utilizator);
        }
    }

    /**
     * Method that loads all friends
     * @throws SQLException
     */
    private void loadFriends() throws SQLException{
        String sql = "SELECT * FROM friendships";
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            //Long id = resultSet.getLong("id");
            Long idUser1 = resultSet.getLong("id_user1");
            Long idUser2 = resultSet.getLong("id_user2");

            User utilizator = (User) findOne((ID) idUser1);
            utilizator.addFriend((User) findOne((ID) idUser2));

            entities.put((ID) idUser1, (E) utilizator);
        }
    }

    /**
     * Method that loads all messages
     * @throws SQLException
     */
    private void loadMessages() throws SQLException{
        String sql = "SELECT * FROM messages";
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {

            Long idUser1 = resultSet.getLong("id_user1");
            Long idUser2 = resultSet.getLong("id_user2");
            String message = resultSet.getString("content");
            Date sqlDate = new Date(resultSet.getDate("date").getTime());
            Long id = resultSet.getLong("id");

            User utilizator1 = (User) findOne((ID) idUser1);
            //User utilizator2 = (User) findOne((ID) idUser2);
            Message msg = new Message(id,utilizator1,utilizator1.getFriends(),message,sqlDate);
            messages.put(id,msg);

        }
    }

    /**
     *
     * @return All the existing users
     */
    @Override
    public Iterable<E> findAll() {
        Set<E> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("user_name");
                String password = resultSet.getString("password");

                User utilizator = new User(firstName, lastName, username, password);
                utilizator.setId(id);
                users.add((E) utilizator);
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users ;

    }

    /**
     * Throws RepoException if the entity is null
     * @param entity The User that we want to save
     *         entity must be not null
     * @return The entity if one is already existing in the repository, null otherwise
     */
    @Override
    public E save(E entity) {
        validator.validate(entity);
        String sql = "insert into users (id, first_name, last_name, user_name, password) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            User user = (User) entity;
            ps.setLong(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getUserName());
            ps.setString(5, user.getPassword());

            ps.executeUpdate();
            load();
            loadFriends();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Throws RepoException if the id is null
     * @param id The id that we will search for in order to delete that User
     *      id must be not null
     * @return The removed entity, null otherwise
     */
    @Override
    public E delete(ID id) {
        String sql = "delete from users where id = " + id;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            //ps.setLong(1,id);
            ps.executeUpdate();
            String sql2 = "delete from friendships where id_user1 = " + id + " or id_user2 = " + id;
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.executeUpdate();

            load();
            loadFriends();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Throws RepoException if the entity is null
     * @param entity the new entity
     *          entity must not be null
     * @return null if it can be updated, entity otherwise
     */
    @Override
    public E update(E entity) {
        validator.validate(entity);
        String sql = "update users set first_name=?, last_name=? where id = ? ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            User user = (User) entity;
            ps.setLong(1,user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.executeUpdate();

            load();
            loadFriends();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getSize() {
        int size=0;
        String sql = "select count(*) from users ";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                size++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public Set<ID> getKeys() {
        Set<ID> set = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                ID ID = (ID) id;
                set.add(ID);
            }
            return set;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;

    }

    /**
     *
     * @param idUser1 The id of one User
     * @param idUser2 The id of another user
     */
    @Override
    public void addFriendRepo(ID idUser1, ID idUser2) throws IOException {

        String sql = "insert into friendships (id_user1, id_user2, friendship_date ) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            java.util.Date javaDate = new java.util.Date();
            java.sql.Date mySQLDate = new java.sql.Date(javaDate.getTime());
            ps.setLong(1, (Long) idUser1);
            ps.setLong(2, (Long) idUser2);
            ps.setDate(3, mySQLDate);
            ps.executeUpdate();

            load();
            loadFriends();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param idUser1 The id of one User
     * @param idUser2 The id of another user
     */
    @Override
    public void deleteFriendRepo(ID idUser1, ID idUser2) throws IOException {

        String sql = "delete from friendships where (id_user1, id_user2) = (?, ?) ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, (Long) idUser1);
            ps.setLong(2, (Long) idUser2);
            ps.executeUpdate();
            load();
            loadFriends();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all the friends for one given user
     * @param idUser The id that we will search for in order to get all friends
     *        id must be not null
     * @return A list with all the friends
     */
    @Override
    public List<E> findAllFriendsForOneUser(ID idUser) {
        List<E> list = new ArrayList<>();
        User user = (User) findOne(idUser);
//        System.out.println("User: " + user.getFirstName() + " " + user.getLastName());
//        System.out.println("-- Friend list --");
        String sql = "SELECT * FROM friendships where id_user1 = " + idUser;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id_user2");
                User friend = (User) findOne((ID) id);
                Date sqlDate = new Date(resultSet.getDate("friendship_date").getTime());
                String date = new SimpleDateFormat("yyyy/MM/dd").format(sqlDate);
                friend.setDate(sqlDate);
                //System.out.println("User: " + friend.getFirstName() + " " + friend.getLastName() + " " + date);
                list.add((E) friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Finds one given friend of one given user
     * @param idUser1 The id that we will search for in order to get a friend
     * @param idUser2 The id of the friend that we search for
     * @return Returns the entity friend
     */
    @Override
    public E findOneFriendForOneUser(ID idUser1, ID idUser2) {
        String sql = "SELECT * FROM friendships where id_user1 = " + idUser1 + " and id_user2 = " + idUser2;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id_user2");
                User user = (User) findOne((ID) id);
                System.out.println("User: " + user.getFirstName() + " " + user.getLastName());
                return (E) user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that creates a message and saves it
     * @param idUser1 The id of the user that sent the message
     * @param idUser2 The id of the friend who will receive the message
     * @param msg The content of the message
     * @throws SQLException
     * @return
     */
    @Override
    public void sendMessageRepo(ID idUser1, ID idUser2, String msg) throws SQLException {
        String sql = "insert into messages (id_user1, id_user2, content, date) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, (Long) idUser1);
            ps.setLong(2, (Long) idUser2);
            ps.setString(3, msg);
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentTime = calendar.getTime();
            long time = currentTime.getTime();
            ps.setTimestamp(4,new Timestamp(time));
            ps.executeUpdate();

            loadMessages();
        }
    }

    /**
     * Method that updates the message's reply
     * @param idUser1 ID of the user that sent the first message
     * @param idUser2 ID of the friend who got the message
     * @param reply Message type
     * @throws SQLException
     */
    private void updateMsg(ID idUser1, ID idUser2, Message reply) throws SQLException {
        String sql = "update messages set reply = ? where id_user1 = ? and id_user2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(reply));
            ps.setLong(2, (Long) idUser1);
            ps.setLong(3, (Long) idUser2);
            ps.executeUpdate();
            loadMessages();
        }
    }


    /**
     * Method that replies to a message
     * @param idUser The id of the user that will reply to the message
     * @param idFriend The id of the friend who sent the first message
     * @param msg The content of the message
     * @throws SQLException
     */
    @Override
    public void replyMessageRepoDb(ID idUser, ID idFriend, String msg) throws SQLException {
        Message replymsg;
        sendMessageRepo(idUser,idFriend,msg);
        //we search for the message to which we reply
        String sql = "SELECT id FROM messages where id_user1 = " + idFriend + " and id_user2 = " + idUser;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                //we get the reply we created
                replymsg = getReply(idUser,idFriend,msg);
                //we set the reply of the message
                messages.get(id).setReply(replymsg);

                updateMsg(idFriend, idUser, replymsg);
//                System.out.println(messages.get(id).getMessage());
//                System.out.println(replymsg.getMessage());
//                System.out.println(messages.get(id).getReply());
            }
        }
    }

    /**
     * Method that gets the reply
     * @param idUser the id of the user who replied
     * @param idFriend the id of the friend who got the reply
     * @param content the content of the message
     * @return returns the reply of type Message
     * @throws SQLException
     */
    private Message getReply(ID idUser, ID idFriend, String content) throws SQLException{
        Message reply = null;
        String sql = "SELECT id FROM messages where id_user1 = " + idUser + " and id_user2 = " + idFriend + " and content = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, content);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                reply = messages.get(id);
            }
        }
        return reply;
    }


    /**
     * Method that shows all the messages of two users
     * @param idUser1 The id of the first user
     * @param idUser2 The id of the second user
     */
    @Override
    public void showMessages(Long idUser1, Long idUser2) {
        String sql = "SELECT * FROM messages \n" +
                " WHERE id_user1 = " + idUser1 + " and id_user2 = " + idUser2 +
                " or id_user1 = " + idUser2 +  " and id_user2 = " + idUser1 + "ORDER BY date ASC";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                System.out.println(messages.get(id).getFrom().getFirstName() +" "+ messages.get(id).getFrom().getLastName()
                        + "\n" + messages.get(id).getMessage() + " | " + messages.get(id).getData() + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Iterable<Message> getAllMessages() {
        Set<Message> msg = new HashSet<>();
        List<User> usersTO = new ArrayList<>();
        Message replymsg;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id_user1");
                User user = (User) findOne((ID) id1);
                Long id2 = resultSet.getLong("id_user2");
                User user2 = (User) findOne((ID) id2);
                usersTO.add(user2);
                String content = resultSet.getString("content");
                Date date = resultSet.getDate("date");

                Message message = new Message(id, user, usersTO, content, date);
                replymsg = getReply((ID) id1,(ID) id2,content);
                message.setReply(replymsg);
                msg.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg ;

    }

    @Override
    public Iterable<Message> listMessagesUsers(ID idUser1, ID idUser2) {
        Set<Message> msg = new HashSet<>();
        List<User> usersTO = new ArrayList<>();
        Message replymsg;
        String sql = "SELECT * FROM messages \n" +
                " WHERE id_user1 = " + idUser1 + " and id_user2 = " + idUser2 +
                " or id_user1 = " + idUser2 +  " and id_user2 = " + idUser1 + "ORDER BY date";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id_user1");
                User user = (User) findOne((ID) id1);
                Long id2 = resultSet.getLong("id_user2");
                User user2 = (User) findOne((ID) id2);
                usersTO.add(user2);
                String content = resultSet.getString("content");
                Date date = resultSet.getDate("date");

                Message message = new Message(id, user, usersTO, content, date);
                replymsg = getReply((ID) id1,(ID) id2,content);
                message.setReply(replymsg);
                msg.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg ;
    }

    /**
     * Finds all friends of a user after given month
     * @param idUser The id of the user where we search all friends
     * @param month The month in which we search all friendships
     * @return Return list of users
     */
    @Override
    public List<E> findAllFriendsForOneUserByMonth(ID idUser, Integer month){
        List<E> list = new ArrayList<>();
        User user = (User) findOne(idUser);

        String sql = "SELECT * FROM friendships where id_user1 =  " + idUser + "AND EXTRACT(MONTH FROM friendship_date) =  " + month;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id_user2");
                User friend = (User) findOne((ID) id);
                Date sqlDate = new Date(resultSet.getDate("friendship_date").getTime());
                friend.setDate(sqlDate);

                list.add((E) friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Method to send a friendship request
     * @param idUser the id of the user that sends the request
     * @param idFriend the id of the user that receives the request
     */
    @Override
    public void sendFriendshipRequest(ID idUser, ID idFriend) {
        String sql = "insert into request (id_user1, id_user2, date) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, (Long) idUser);
            ps.setLong(2, (Long) idFriend);
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentTime = calendar.getTime();
            long time = currentTime.getTime();
            ps.setTimestamp(3,new Timestamp(time));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to answer a friendship request
     * @param idUser the id of the user that answers the request
     * @param idFriend the id of the user that sent the request
     * @param raspuns the answer to the request
     *                if "approved" then save new friendship between the two users
     */
    @Override
    public void answerFriendshipRequest(ID idUser, ID idFriend, String raspuns) {
        String sql = "update request set status = ? where id_user1 = ? AND id_user2 = ? ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,raspuns);
            ps.setLong(2, (Long) idFriend);
            ps.setLong(3, (Long) idUser);

            if (raspuns.equals("approved"))
                addFriendRepo(idFriend, idUser);

            ps.executeUpdate();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Throws RepoException if the id is null
     * @param user_name the username of the entity to be returned
     *      *           username must not be null
     * @return the entity with the specified username
     */
    @Override
    public E findOneByUsername(String user_name, String pwd) {
        String sql = "SELECT * FROM users WHERE user_name = '" + user_name + "' AND password = '" + pwd +"'";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("user_name");
                String password = resultSet.getString("password");

                User utilizator = new User(firstName, lastName, username, password);
                utilizator.setId(resultSet.getLong("id"));
                return entities.get(utilizator.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Throws RepoException if the entity is null
     * @param entity The User that we want to save
     *         entity must be not null
     * @return The entity if one is already existing in the repository, null otherwise
     */
    @Override
    public E saveUser(E entity) {
        validator.validate(entity);
        String sql = "insert into users (first_name, last_name, user_name, password ) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            User user = (User) entity;

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getUserName());
            ps.setString(4, user.getPassword());

            ps.executeUpdate();
            load();
            loadFriends();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return null;
    }
}
