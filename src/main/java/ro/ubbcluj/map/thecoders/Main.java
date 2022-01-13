package ro.ubbcluj.map.thecoders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ro.ubbcluj.map.thecoders.controller.UsersController;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.UserValidator;
import ro.ubbcluj.map.thecoders.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.thecoders.repository.paging.PagingRepository;
import ro.ubbcluj.map.thecoders.services.Service;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    PagingRepository<Long, User> repository;
    Service service;

    double x,y=0;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException, SQLException {
//        String fileNameUsers = "data/users.csv";
//        String fileNameFriendships = "data/friendships.csv";
//        repository = new UserFile(fileNameUsers, fileNameFriendships, new UserValidator());
        repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        service = new Service(repository);

        initView(stage);
        stage.show();
    }

    private void initView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ro/ubbcluj/map/thecoders/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 520, 400);
        stage.initStyle(StageStyle.UNDECORATED);

        //move around
//        root.setOnMousePressed(evt -> {
//              x = evt.getSceneX();
//              y = evt.getSceneY();
//        });
//        root.setOnMousePressed(evt -> {
//              stage.setX(evt.getScreenX()- x);
//              stage.setY(evt.getScreenY()- y);
//        });

        stage.setScene(scene);

//        UsersController usersController = fxmlLoader.getController();
//        usersController.setService(service);
    }

}