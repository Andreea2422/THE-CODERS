package ro.ubbcluj.map.thecoders.repository.file;

import ro.ubbcluj.map.thecoders.domain.Entity;
import ro.ubbcluj.map.thecoders.domain.validators.ValidationException;
import ro.ubbcluj.map.thecoders.domain.validators.Validator;
import ro.ubbcluj.map.thecoders.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    String fileFriendshipName;
    public AbstractFileRepository(String fileName,String fileFriendshipName, Validator<E> validator) throws ValidationException, IOException {
        super(validator);
        this.fileName = fileName;
        this.fileFriendshipName=fileFriendshipName;
        loadData();
    }

    /**
     * The method that loads the users from a file
     * @throws IOException if the file could not be open
    */
    private void loadData() throws ValidationException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attributes = Arrays.asList(linie.split(";"));
                //System.out.println("ok");
                E e = extractEntity(attributes);
                super.save(e);
            }
            loadFriendships();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the friendships between users from a file
     * @throws IOException if the file could not be open
     */
    private void loadFriendships(){
        try (BufferedReader br = new BufferedReader(new FileReader(fileFriendshipName))) {
            String line;
            while((line = br.readLine()) != null && !line.equals("")){
                List<String> attributes = Arrays.asList(line.split(";"));
                addFriendshipsFromFile(attributes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the friendships between users to file
     * @throws IOException if the file could not be open
     */
    private void writeFriendships(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileFriendshipName, false))) {
            for(E e : findAll()){
                bw.write(createFriendshipsAsString(e));
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    protected abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    /**
     *
     * @param entity The entity to be written to file
     * @return The transformed entity to a string
     */
    protected abstract String createEntityAsString(E entity);

    /**
     *
     * @param entity The entity of which friendships to be written to file
     * @return The friendship transformed to a string
     */
    protected abstract String createFriendshipsAsString(E entity);

    /**
     * Adds the friendship written from file to the corresponding users
     * @param attributes The ids of the users
     * @throws IOException if it could not write to file
     */
    protected abstract void addFriendshipsFromFile(List<String> attributes) throws IOException;

    /**
     *
     * @param entity - The entity to be saved in the file
     * @return entity if it could not be saved else return null
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public E save(E entity) throws ValidationException {
        if (super.save(entity) != null){
            return entity;
        }
        writeToFile(entity);
        writeFriendships();
        return null;
    }

    public void addFriendRepo(ID id1, ID id2) throws IOException {
        super.addFriendRepo(id1, id2);
        writeFriendships();
    }

    public void deleteFriendRepo(ID id1, ID id2) throws IOException {
        super.deleteFriendRepo(id1, id2);
        writeFriendships();
    }


    /**
     *
     * @param id The id of the user to be deleted
     * @return null if the operation succeeded else return the entity
     */
    @Override
    public E delete(ID id){
        if(super.delete(id) == null){
            return findOne(id);
        }
        writeEntities();
        writeFriendships();
        return null;

    }

    /**
     *
     * @param entity the new Entity to be saved to file(updated)
     * @return null if the operation was successful else return the entity
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public E update(E entity) throws ValidationException {
        if (super.update(entity) != null){
            return entity;
        }
        writeEntities();
        writeFriendships();
        return null;
    }

    private void writeEntities(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false))) {
            for (E e: findAll()){
                bw.write(createEntityAsString(e));
                bw.newLine();
            }
            bw.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    protected void writeToFile(E entity){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(createEntityAsString(entity));
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


