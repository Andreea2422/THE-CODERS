package ro.ubbcluj.map.thecoders.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ro.ubbcluj.map.thecoders.Main;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.UserValidator;
import ro.ubbcluj.map.thecoders.repository.Repository;
import ro.ubbcluj.map.thecoders.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.thecoders.services.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class SceneController {
    Service service;
    User user;

    @FXML
    private Button backButton;
    @FXML
    private Label eventName;
    @FXML
    private Label statusLabel;
    @FXML
    private ImageView eventImageView1;
    @FXML
    private ImageView prevImageView;
    @FXML
    private ImageView nextImageView;


    public void setUser(User user) {
        this.user = user;
    }

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    public void initialize() throws Exception{
        File logoFile = new File("Images/concert.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        eventImageView1.setImage(logoImage);

        File chatsFile = new File("Images/previous.png");
        Image chatsImage = new Image(chatsFile.toURI().toString());
        prevImageView.setImage(chatsImage);

        File eventsFile = new File("Images/next.png");
        Image eventsImage = new Image(eventsFile.toURI().toString());
        nextImageView.setImage(eventsImage);


    }

    public void switchToScene1(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("scene1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        service = new Service(repository);
        SceneController sceneController = fxmlLoader.getController();
        sceneController.setUser(user);
        sceneController.setService(service);

        registerStage.setScene(scene);
        registerStage.show();
    }

    public void switchToScene2(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("scene2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        service = new Service(repository);
        SceneController sceneController = fxmlLoader.getController();
        sceneController.setUser(user);
        sceneController.setService(service);
        File userFile = new File("Images/exhibit.png");
        Image userImage = new Image(userFile.toURI().toString());
        sceneController.eventImageView1.setImage(userImage);
        registerStage.setScene(scene);
        registerStage.show();
    }

    public void switchToScene3(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("scene3.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        service = new Service(repository);
        SceneController sceneController = fxmlLoader.getController();
        sceneController.setUser(user);
        sceneController.setService(service);
        registerStage.setScene(scene);
        File userFile = new File("Images/info.png");
        Image userImage = new Image(userFile.toURI().toString());
        sceneController.eventImageView1.setImage(userImage);
        registerStage.show();
    }

    public void subscribeButtonOnAction(ActionEvent event){
        String name = eventName.getText();
        service.saveSubscription(user.getId(),name);
        statusLabel.setText("You have subscribed to the event! :)");
    }

    public void unsubscribeButtonOnAction(ActionEvent event){
        String name = eventName.getText();
        service.deleteSubscription(user.getId(),name);
        statusLabel.setText("You have unsubscribed from the event! :(");
    }

    public void backButtonOnAction(ActionEvent event){
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

}
