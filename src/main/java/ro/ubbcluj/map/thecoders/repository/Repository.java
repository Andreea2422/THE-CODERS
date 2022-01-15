package ro.ubbcluj.map.thecoders.repository;

import ro.ubbcluj.map.thecoders.domain.Entity;
import ro.ubbcluj.map.thecoders.domain.Events;
import ro.ubbcluj.map.thecoders.domain.Message;
import ro.ubbcluj.map.thecoders.domain.validators.ValidationException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface Repository<ID, E extends Entity<ID>> {

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    E findOne(ID id);

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();

    List<E> findAllFriendsForOneUser(ID id);

    List<E> findAllRequestsForOneUser(ID id);

    E findOneFriendForOneUser(ID id1, ID id2);

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    E save(E entity);


    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    E delete(ID id);

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    E update(E entity);

    int getSize();

    Set<ID> getKeys();

    void addFriendRepo(ID idUser1, ID idUser2) throws IOException;

    void deleteFriendRepo(ID idUser1, ID idUser2) throws IOException;

    /**
     * One user send message to one friend.
     * @param idUser1
     * @param idUser2
     * @param msg
     * @return
     */
    void sendMessageRepo(ID idUser1, ID idUser2, String msg) throws SQLException;

    void sendFriendshipRequest(ID idUser, ID idFriend);

    void answerFriendshipRequest(ID idUser, ID idFriend, String raspuns);

    void replyMessageRepoDb(ID idUser1, ID idUser2, String rasp) throws SQLException;

    void showMessages(Long idUser, Long idFriend);

    List<E> findAllFriendsForOneUserByMonth(ID idUser, Integer month) ;

    /**
     * Throws RepoException if the id is null
     * @param user_name the username of the entity to be returned
     *      *           username must not be null
     * @return the entity with the specified username
     */
    E findOneByUsername(String user_name, String password);

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    E saveUser(E entity);

    Iterable<Message> getAllMessages();
    Iterable<Message> listMessagesUsers(ID idUser1, ID idUser2);
    void saveSubscription(ID idUser, String event);
    void deleteSubscription(ID idUser, String event);
    List<Events> getAllEvents(ID idUser);


}
