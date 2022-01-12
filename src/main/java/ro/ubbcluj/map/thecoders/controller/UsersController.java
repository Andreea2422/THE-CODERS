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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UsersController implements Observer<UserChangeEvent>{
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();
    private String username;
    private User user;

    @FXML
    private Button signOutButton;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    TableView<User> tableView;
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
        Iterable<User> users = service.getAll();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }


    public void signOutButtonOnAction(ActionEvent event){
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.close();
    }

    public void addButtonOnAction(ActionEvent event) throws IOException {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.addFriendServ(user.getId(),selected.getId());

        } else
            MessageAlert.showErrorMessage(null, "NU ati selectat nici un student");

    }

    public void removeButtonOnAction(ActionEvent event){}

    public void setUser(User user) {
        this.user = user;
    }
}
