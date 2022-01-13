package ro.ubbcluj.map.thecoders.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private Button usersButton;
//    @FXML
//    private Button chatsButton;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView groupImageView;
    @FXML
    private ImageView chatsImageView;

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
//       tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
//       tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
//       tableColumnUserName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
//       tableColumnPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
//       tableView.setItems(model);

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

        registerStage.setScene(scene);
        registerStage.show();

    }


}
