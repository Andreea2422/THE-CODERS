package ro.ubbcluj.map.thecoders.controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatController implements Observer<UserChangeEvent> {
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();
    private String username;
    User user;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button returnButton;
    @FXML
    TableView<User> tableViewChat;
    @FXML
    TableColumn<User, String> tableColumnChats;

    public ChatController() throws SQLException {}

    public void setService(Service service){
        this.service = service;
        this.service.addObserver(this);
        initModel();

    }

    private void initModel() {
        Iterable<User> users = service.getAll();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);

    }

    @FXML
    public void initialize() throws Exception {
        tableColumnChats.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new SimpleStringProperty(param.getValue().getUserName());
            }
        });

        tableViewChat.setItems(model);
        selectedUser();

    }

    public void returnButtonOnAction(ActionEvent event){
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void selectedUser() throws SQLException {
        Repository<Long, User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        service = new Service(repository);

        tableViewChat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ro/ubbcluj/map/thecoders/msg-view.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 487, 386);

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

                User selected = tableViewChat.getSelectionModel().getSelectedItem();


                MsgController msgController = fxmlLoader.getController();
                msgController.setUser(user);
                msgController.setFriend(selected);
                msgController.setService(service);


                registerStage.setScene(scene);
                registerStage.show();
            } catch (IOException e) {
            e.printStackTrace(); }
        }});

    }
}
