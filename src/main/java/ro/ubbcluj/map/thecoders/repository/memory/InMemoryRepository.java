package ro.ubbcluj.map.thecoders.repository.memory;


import ro.ubbcluj.map.thecoders.domain.Entity;
import ro.ubbcluj.map.thecoders.domain.Message;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.ValidationException;
import ro.ubbcluj.map.thecoders.domain.validators.Validator;
import ro.ubbcluj.map.thecoders.repository.RepoException;
import ro.ubbcluj.map.thecoders.repository.paging.Page;
import ro.ubbcluj.map.thecoders.repository.paging.Pageable;
import ro.ubbcluj.map.thecoders.repository.paging.PagingRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class InMemoryRepository<ID, E extends Entity<ID>> implements PagingRepository<ID,E> {

    private Validator<E> validator;
    private Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    /**
     * Throws RepoException if the id is null
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return The entity with the specified id
     */
    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("ID must be not null");
        return entities.get(id);
    }

    /**
     *
     * @return All the existing users
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public List<E> findAllFriendsForOneUser(ID id) {
        List<E> list = new ArrayList<>();
        User user = (User) findOne(id);
        if (user!=null) {
//            System.out.println("User: " + user.getFirstName() + " " + user.getLastName());
//            System.out.println("-- Lista de prieteni --");
            for (User friend : user.getFriends()) {
                //System.out.println("User: " + friend.getFirstName() + " " + friend.getLastName());
                User userFriend = (User) findOne((ID) friend.getId());
                list.add((E) userFriend);
            }
        }
        return list;
    }

    @Override
    public List<E> findAllRequestsForOneUser(ID id) {
        return null;
    }

    @Override
    public E findOneFriendForOneUser(ID idUser1, ID idUser2) {
        int ok=0;
        if (idUser1==null || idUser2==null)
            throw new IllegalArgumentException("ID must be not null");
        User user = (User) findOne(idUser1);
        User friendCautat = (User) findOne(idUser2);
        if (user!=null) {
            for (User friend : user.getFriends()) {
                //System.out.println("User: " + friend.getFirstName() + " " + friend.getLastName());
                Long idFriend = friend.getId();
                User userFriend = (User) findOne((ID) idFriend);

                if(friendCautat == userFriend) {
                    ok = 1;
                    System.out.println("User: " + friend.getFirstName() + " " + friend.getLastName());
                }

            }
        }
        if(ok==0){
            System.out.println("The two users are not friends");
        }
        return null;
    }

    /**
     * Throws RepoException if the entity is null
     * @param entity The User that we want to save
     *         entity must be not null
     * @return The entity if one is already existing in the repository, null otherwise
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public E save(E entity) throws ValidationException {
        if (entity==null)
            throw new RepoException("Entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return entity;
        }
        else entities.put(entity.getId(),entity);
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
        if(entities.get(id) != null) {
            User entity = (User) entities.get(id);
            entity.getFriends().clear();
            for (ID k : getKeys()) {
                User user = (User) findOne(k);
                if (user.getFriends().contains(entity)) {
                    user.getFriends().remove(entity);
                }
            }
            entities.remove(id);
            return (E) entity;
        }
        if(id == null)
            throw new RepoException("The entity was not found!");
        return null;

    }

    /**
     * Throws RepoException if the entity is null
     * @param entity the new entity
     *          entity must not be null
     * @return null if it can be updated, entity otherwise
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public E update(E entity) throws ValidationException {

        if(entity == null)
            throw new RepoException("Entity must be not null!");
        validator.validate(entity);

        if(entities.get(entity.getId()) != null) {
            E e = entities.put(entity.getId(),entity);
            User user = (User) e;
            User entitate = (User) entity;
            if(user!=null)
            {
                entitate.setFriends(user.getFriends());
                for(User users:user.getFriends())
                {
                    for(User user1:users.getFriends())
                    {
                        if(Objects.equals(user1.getId(),user.getId()))
                        {
                            user1.setFirstName(entitate.getFirstName());
                            user1.setLastName(entitate.getLastName());
                        }
                    }
                }
            }
            return null;
        }

        return entity;

    }

    @Override
    public int getSize() {
        return entities.size();
    }

    @Override
    public Set<ID> getKeys() {
        return entities.keySet();
    }

    /**
     *
     * @param idUser1 The id of one User
     * @param idUser2 The id of another user
     */
    @Override
    public void addFriendRepo(ID idUser1, ID idUser2) throws IOException {
        User user = (User) findOne(idUser1);
        User user2 = (User) findOne(idUser2);
        if(user!=null&&user2!=null)
            user.addFriend((User) findOne(idUser2));
    }

    /**
     *
     * @param idUser1 The id of one User
     * @param idUser2 The id of another user
     */
    @Override
    public void deleteFriendRepo(ID idUser1, ID idUser2) throws IOException {
        User user = (User) findOne(idUser1);
        if(user!=null)
            user.deleteFriend((Long) idUser2);
    }

    @Override
    public void sendMessageRepo(ID idUser1, ID idUser2, String msg) throws SQLException {

    }

    @Override
    public void replyMessageRepoDb(ID idUser1, ID idUser2, String rasp) throws SQLException {

    }

    @Override
    public void showMessages(Long idUser, Long idFriend) {

    }

    @Override
    public List<E> findAllFriendsForOneUserByMonth(ID idUser, Integer month) {
        return null;
    }

    @Override
    public E findOneByUsername(String user_name, String password) {
        return entities.get(user_name);
    }

    @Override
    public E saveUser(E entity) {
        return null;
    }

    @Override
    public Iterable<Message> getAllMessages() {
        return null;
    }

    @Override
    public Iterable<Message> listMessagesUsers(ID idUser1, ID idUser2) {
        return null;
    }

    @Override
    public void sendFriendshipRequest(ID idUser, ID idFriend) {

    }

    @Override
    public void answerFriendshipRequest(ID idUser, ID idFriend, String raspuns) {

    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return null;
    }
}

