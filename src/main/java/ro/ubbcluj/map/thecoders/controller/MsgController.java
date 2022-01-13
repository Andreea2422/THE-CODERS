package ro.ubbcluj.map.thecoders.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.ubbcluj.map.thecoders.domain.Message;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.services.Service;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MsgController implements Observer<UserChangeEvent> {
      Service service;
      ObservableList<User> model = FXCollections.observableArrayList();
      private String username;
      private User user;

      @FXML
      private Button sendButton;
      @FXML
      private Button goBackButton;
      @FXML
      private TextField messageTextField;
      @FXML
      TableView<Message> tableViewMsg;
      @FXML
      TableColumn<Message, String> friendColumn;
      @FXML
      TableColumn<Message, String> userColumn;

      public MsgController() throws SQLException {}


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

        }

      public void goBackOnAction(ActionEvent event){
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            stage.close();
        }


      public void Stuff(){
            String message = messageTextField.getText();
            //User user = repository.findOneByUsername(messageTextField.getText(), messageTextField.getText());
        }

      public void sendButtonOnAction(ActionEvent event){
            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.close();
        }

      @Override
        public void update(UserChangeEvent userChangeEvent) {
            initModel();
        }


}
