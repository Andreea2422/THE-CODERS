package ro.ubbcluj.map.thecoders.services;

import ro.ubbcluj.map.thecoders.domain.Entity;
import ro.ubbcluj.map.thecoders.domain.Events;
import ro.ubbcluj.map.thecoders.domain.Message;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.repository.Repository;
import ro.ubbcluj.map.thecoders.repository.paging.Page;
import ro.ubbcluj.map.thecoders.repository.paging.Pageable;
import ro.ubbcluj.map.thecoders.repository.paging.PageableImplementation;
import ro.ubbcluj.map.thecoders.repository.paging.PagingRepository;
import ro.ubbcluj.map.thecoders.utils.events.ChangeEventType;
import ro.ubbcluj.map.thecoders.utils.events.MessageChangeEvent;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observable;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Service<ID, E extends Entity<ID>> implements Observable<MessageChangeEvent> {
    private Repository<ID, E> repository;
    public Service(Repository<ID, E> repository){
        this.repository = repository;
    }
    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();

    public Iterable<Message> getAllMessages(){
        return repository.getAllMessages();
    }

    public Iterable<Message> listMessagesUsers(ID idUser1, ID idUser2) {
        return repository.listMessagesUsers(idUser1,idUser2);
    }


    public Iterable<E> getAll(){
        return repository.findAll();
    }

    /**
     * Shows the friends of one user
     * @param id The id that we will search in the repo
     * @return the friends associated with that id, null otherwise
     */
    public List<E> allFriendsForOneUser(ID id){
        return repository.findAllFriendsForOneUser(id);
    }

    /**
     * Shows the requests of one user
     * @param id The id that we will search in the repo
     * @return the request associated with that id, null otherwise
     */
    public List<E> allRequestsForOneUser(ID id){
        return repository.findAllRequestsForOneUser(id);
    }

    /**
     * Shows one friend of one user
     * @param id1 The id that we will search in the repo
     * @param id2 The id of the friend that we will search in the repo
     * @return the entity associated with that id, null otherwise
     */
    public E oneFriend(ID id1, ID id2){
        return repository.findOneFriendForOneUser(id1,id2);
    }

    /**
     * Shows the friends of one user by given month
     * @param idUser The id that we will search in the repo
     * @param month the month of creating friendship
     * @return the friends associated with that id and month, null otherwise
     */
    public List<E> allFriendsForOneUserByMonth(ID idUser, Integer month)  {
        return repository.findAllFriendsForOneUserByMonth(idUser, month);
    }


    /**
     * @param entity The User that we want to save in the repository
     * @return null if the operation is successful, entity otherwise
     */
    public E saveServ(E entity) {
        return repository.save(entity);
    }

    /**
     * @param entity The User that we want to save in the repository
     * @return null if the operation is successful, entity otherwise
     */
    public E saveUserServ(E entity) {
        return repository.saveUser(entity);
    }

    /**
     *
     * @param id The id to be searched in order to delete the User with the corresponding id
     * @return null if successful, entity otherwise
     */
    public E deleteServ(ID id){
        return repository.delete(id);
    }

    /**
     *
     * @param id The id that we will search in the repo
     * @return the entity associated with that id, null otherwise
     */
    public E findOneServ(ID id){
        return repository.findOne(id);
    }

    /**
     *
     * @return The size of the repository(how many Users)
     */
    public int getSizeServ(){
        return repository.getSize();
    }

    /**
     *
     * @return The set of the Ids
     */
    public Set<ID> getKeysServ(){
        return repository.getKeys();
    }

    /**
     *
     * @param entity The new modified entity
     * @return null if the operation is successful, entity otherwise
     */
    public E updateServ(E entity){
        return repository.update(entity);
    }

    /**
     * The method that establishes a friendship relation between two Users
     * @param id1 The id of a User
     * @param id2 The id of another User
     */
    public void addFriendServ(ID id1, ID id2) throws IOException {
        repository.addFriendRepo(id1,id2);
    }

    /**
     * Method that deletes a friend from a user
     * @param id1 The id of a User
     * @param id2 The id of another user
     */
    public void deleteFriendServ(ID id1, ID id2) throws IOException {
        repository.deleteFriendRepo(id1,id2);
    }

    /**
     * One user send message to a friend
     * @param idUser The id that will send message
     * @param idFriend The id of the friend that receive message
     * @param message Content of the message
     */
    public void sendOneMessage(ID idUser, ID idFriend, String message) throws SQLException {
        repository.sendMessageRepo(idUser,idFriend,message);

    }

    /**
     * Reply to a message
     * @param idUser the id that will reply
     * @param idFriend the id that sent the first message
     * @param raspuns Content of the reply
     */
    public void replyOneMessage(ID idUser, ID idFriend, String raspuns) throws SQLException {
        repository.replyMessageRepoDb(idUser,idFriend,raspuns);
    }

    /**
     * Show conversation for two users
     * @param idUser the id of the first user
     * @param idFriend the id of the second user
     */
    public void showConvoForTwoUsers(Long idUser, Long idFriend) {
        repository.showMessages(idUser,idFriend);
    }

    /**
     * One user send request friendship to a friend
     * @param idUser the id of the user who sent the request
     * @param idFriend the id of the user who received the request
     */
    public void requestFriendshipById(ID idUser, ID idFriend) {
        repository.sendFriendshipRequest(idUser, idFriend);
    }

    /**
     * Answer the friendship request
     * @param idUser the id of the user who sent the request
     * @param idFriend the id of the user who received the request
     * @param raspuns the answer to the friendship request
     */
    public void answerFriendshipById(ID idUser, ID idFriend, String raspuns) {
        repository.answerFriendshipRequest(idUser, idFriend, raspuns);
    }


    @Override
    public void addObserver(Observer<MessageChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> e) {

    }

    @Override
    public void notifyObservers(MessageChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    public void saveSubscription(ID idUser, String event) {
        repository.saveSubscription(idUser,event);
    }

    public void deleteSubscription(ID idUser, String event) {
        repository.deleteSubscription(idUser,event);
    }

    public List<Events> getAllEvents(ID idUser){
            return repository.getAllEvents(idUser);

    }
}
