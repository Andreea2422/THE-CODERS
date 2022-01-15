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
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class EventsController {
    Service service;
    User user;

    @FXML
    private Button returnButton;
    @FXML
    private ImageView eventsImageView;


    public EventsController() throws SQLException {}

    public void setUser(User user) {
        this.user = user;
    }

    public void setService(Service service){
        this.service = service;
    }

    public void returnButtonOnAction(ActionEvent event){
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() throws Exception{
        File outFile = new File("Images/event.png");
        Image outImage = new Image(outFile.toURI().toString());
        eventsImageView.setImage(outImage);

    }




    public void myButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ro/ubbcluj/map/thecoders/myevents.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        Stage registerStage = new Stage();
        registerStage.initStyle(StageStyle.UNDECORATED);
        Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        service = new Service(repository);
        MyEventsController myEventsController = fxmlLoader.getController();
        myEventsController.setUser(user);
        myEventsController.setService(service);
        registerStage.setScene(scene);
        registerStage.show();
    }

    public void eventsButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ro/ubbcluj/map/thecoders/scene1.fxml"));
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


}
