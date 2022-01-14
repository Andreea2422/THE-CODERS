package ro.ubbcluj.map.thecoders.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import ro.ubbcluj.map.thecoders.Main;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.UserValidator;
import ro.ubbcluj.map.thecoders.repository.Repository;
import ro.ubbcluj.map.thecoders.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.thecoders.services.Service;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observable;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FunctionalitiesController implements Observer<UserChangeEvent> {
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();
    User user;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button signOutButton;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView groupImageView;
    @FXML
    private ImageView chatsImageView;
    @FXML
    private ImageView signOutImageView;
    @FXML
    private ImageView eventsImageView;
    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnUserName;
    @FXML
    private Label addFriendMessageLabel;
    @FXML
    private TextField searchTextField;

    public void setService(Service service){
        this.service = service;
        this.service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize(){
        File logoFile = new File("Images/logo.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        File userFile = new File("Images/user.png");
        Image userImage = new Image(userFile.toURI().toString());
        userImageView.setImage(userImage);

        File groupFile = new File("Images/group.png");
        Image groupImage = new Image(groupFile.toURI().toString());
        groupImageView.setImage(groupImage);

        File chatsFile = new File("Images/chats.png");
        Image chatsImage = new Image(chatsFile.toURI().toString());
        chatsImageView.setImage(chatsImage);

        File eventsFile = new File("Images/event.png");
        Image eventsImage = new Image(eventsFile.toURI().toString());
        eventsImageView.setImage(eventsImage);

        File outFile = new File("Images/signout.png");
        Image outImage = new Image(outFile.toURI().toString());
        signOutImageView.setImage(outImage);

        tableColumnFirstName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleStringProperty(param.getValue().getFirstName());
            }
        });
        tableColumnLastName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleStringProperty(param.getValue().getLastName());
            }
        });
        tableColumnUserName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleStringProperty(param.getValue().getUserName());
            }
        });
        tableView.setItems(model);

        searchTextField.textProperty().addListener(o -> handleFilter());
    }

    private void initModel() {
        Iterable<User> users = service.getAll();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(userList);
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    public void signOutButtonOnAction(ActionEvent event){
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.close();
    }

    public void usersButtonOnAction(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("users-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(scene);

            Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
            service = new Service(repository);

            UsersController usersController = fxmlLoader.getController();
            usersController.setService(service);
            usersController.setUser(user);

            registerStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void chatsButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ro/ubbcluj/map/thecoders/chat-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 291, 483);
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = registerStage.getX() - event.getScreenX();
                yOffset = registerStage.getY() - event.getScreenY();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                registerStage.setX(event.getScreenX() + xOffset);
                registerStage.setY(event.getScreenY() + yOffset);
            }
        });

        Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        service = new Service(repository);

        ChatController chatController = fxmlLoader.getController();
        chatController.setService(service);
        chatController.setUser(user);

        registerStage.setScene(scene);
        registerStage.show();

    }


    public void addFriendButtonOnAction(ActionEvent event) throws IOException {
        User selected = (User) tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            var friends = service.allFriendsForOneUser(user.getId());
            if(friends.contains(selected)){
                addFriendMessageLabel.setText("Friend already exist!");
            }
            else {
                service.requestFriendshipById(this.user.getId(), selected.getId());
                addFriendMessageLabel.setText("Friend request sent!");
            }
        }
        else {
            addFriendMessageLabel.setText("Select one user!");
        }
    }

    private void handleFilter() {
        Predicate<User> p1 = u -> u.getFirstName().startsWith(searchTextField.getText());
        Predicate<User> p2 = u -> u.getLastName().startsWith(searchTextField.getText());
        Predicate<User> p3 = u -> u.getUserName().startsWith(searchTextField.getText());

        model.setAll(getUsersList()
                .stream()
                .filter(p1.or(p2).or(p3))
                .collect(Collectors.toList()));
    }
    private List<User> getUsersList() {
        Iterable<User> users = service.getAll();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        return userList;
    }


    public void eventsButtonOnAction(ActionEvent event){
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.close();
    }


}
