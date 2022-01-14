package ro.ubbcluj.map.thecoders.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import ro.ubbcluj.map.thecoders.Main;
import ro.ubbcluj.map.thecoders.domain.Request;
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
    ObservableList<User> modelFriends = FXCollections.observableArrayList();
    ObservableList<User> modelRequests = FXCollections.observableArrayList();
    User user;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button signOutButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button requestUsersButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button friendsButton;
    @FXML
    private Button chatsButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Pane statusPane;
    @FXML
    private GridPane searchPane;
    @FXML
    private GridPane friendRequestPane;
    @FXML
    private GridPane friendsPane;
    @FXML
    private GridPane profilePane;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView groupImageView;
    @FXML
    private ImageView chatsImageView;
    @FXML
    TableView<User> tableView;
    @FXML
    TableView<User> tableView1;
    @FXML
    TableView<User> tableView2;
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
    @FXML
    private TextField friendsSearchTextField;
    @FXML
    private TextField requestSearchTextField;

    public void setService(Service service){
        this.service = service;
        this.service.addObserver(this);
        initModel();
        initModelFriends();
        initModelRequests();
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

        tableSearchUsers();
        tableSearchFriends();
        tableSearchRequests();
    }

    private void tableSearchRequests() {
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
        tableView2.setItems(modelRequests);

        requestSearchTextField.textProperty().addListener(o -> handleFilterRequests());
    }

    private void tableSearchFriends() {
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
        tableView1.setItems(modelFriends);

        friendsSearchTextField.textProperty().addListener(o -> handleFilterFriends());
    }

    private void tableSearchUsers() {
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
    private void initModelFriends() {
        List<User> userList = service.allFriendsForOneUser(user.getId());
        modelFriends.setAll(userList);
    }
    private void initModelRequests() {
        List<User> userList = service.allRequestsForOneUser(user.getId());
        modelRequests.setAll(userList);
    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    public void signOutButtonOnAction(ActionEvent event){
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.close();
    }

//    public void requestUsersButtonOnAction(ActionEvent event){
//        try{
//            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("users-view.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
//            Stage registerStage = new Stage();
//            registerStage.initStyle(StageStyle.UNDECORATED);
//            registerStage.setScene(scene);
//
////            Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
////            service = new Service(repository);
//
//            UsersController usersController = fxmlLoader.getController();
//            usersController.setService(service);
//            usersController.setUser(user);
//
//            registerStage.show();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

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
    public void deleteRequestButtonOnAction(ActionEvent event) {
    }

    public void confirmRequestButtonOnAction(ActionEvent event) {
    }

    public void removeFriendButtonOnAction(ActionEvent event) {
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
    private void handleFilterFriends() {
        Predicate<User> p1 = u -> u.getFirstName().startsWith(searchTextField.getText());
        Predicate<User> p2 = u -> u.getLastName().startsWith(searchTextField.getText());
        Predicate<User> p3 = u -> u.getUserName().startsWith(searchTextField.getText());

        model.setAll(getFriendsList()
                .stream()
                .filter(p1.or(p2).or(p3))
                .collect(Collectors.toList()));
    }
    private void handleFilterRequests() {
        Predicate<User> p1 = u -> u.getFirstName().startsWith(searchTextField.getText());
        Predicate<User> p2 = u -> u.getLastName().startsWith(searchTextField.getText());
        Predicate<User> p3 = u -> u.getUserName().startsWith(searchTextField.getText());

        model.setAll(getUsersListRequests()
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
    private List<User> getFriendsList() {
        return service.allFriendsForOneUser(user.getId());
    }
    private List<User> getUsersListRequests() {
       return service.allRequestsForOneUser(user.getId());
    }

    public void searchButtonOnAction(ActionEvent event) {
        statusLabel.setText("Find People");
        statusPane.setBackground(new Background(new BackgroundFill(Color.rgb(173,150,113), CornerRadii.EMPTY, Insets.EMPTY)));
        searchPane.toFront();
    }
    public void profileButtonOnAction(ActionEvent event) {
        statusLabel.setText("My Profile");
        statusPane.setBackground(new Background(new BackgroundFill(Color.rgb(242,193,115), CornerRadii.EMPTY, Insets.EMPTY)));
        profilePane.toFront();
    }
    public void requestUsersButtonOnAction(ActionEvent event){
        statusLabel.setText("Answer Requests");
        statusPane.setBackground(new Background(new BackgroundFill(Color.rgb(173,150,113), CornerRadii.EMPTY, Insets.EMPTY)));
        friendRequestPane.toFront();
    }
    public void friendsButtonOnAction(ActionEvent event){
        statusLabel.setText("My Friends");
        statusPane.setBackground(new Background(new BackgroundFill(Color.rgb(173,150,113), CornerRadii.EMPTY, Insets.EMPTY)));
        friendsPane.toFront();
    }

}
