package ro.ubbcluj.map.thecoders.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.services.Service;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UsersController implements Observer<UserChangeEvent>{
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();
    private String username;

    @FXML
    private Button signOutButton;
    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User, String> tableColumnUserName;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;


    public void setService(Service service){
        this.service = service;
        this.service.addObserver(this);
        initModel();
    }

    public void setLoggedUsername(String loggedUsername) {
        this.username = loggedUsername;
    }


    @FXML
    public void initialize() throws Exception {
        //tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        //tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));

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

        tableView.setItems(model);

    }


    private void initModel() {
        //de modificat

//        Iterable<User> users = service.allFriendsForOneUser(username);
//        List<User> userList = StreamSupport.stream(users.spliterator(), false)
//                .collect(Collectors.toList());
//        model.setAll(userList);

        model.setAll(service.allFriendsForOneUser(1));
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }


    public void signOutButtonOnAction(ActionEvent event){
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.close();
    }
}
