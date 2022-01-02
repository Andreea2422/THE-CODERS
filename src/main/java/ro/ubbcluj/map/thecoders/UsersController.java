package ro.ubbcluj.map.thecoders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.services.Service;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UsersController implements Observer<UserChangeEvent>{
    Service service;
    ObservableList<User> model = FXCollections.observableArrayList();

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

    @FXML
    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        tableView.setItems(model);
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
}
