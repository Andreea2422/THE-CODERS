package ro.ubbcluj.map.thecoders.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.ubbcluj.map.thecoders.domain.Message;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.services.Service;
import ro.ubbcluj.map.thecoders.utils.events.MessageChangeEvent;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MsgController implements Observer<MessageChangeEvent> {
      Service service;
      ObservableList<Message> model = FXCollections.observableArrayList();
      private User user;
      User selected;

      @FXML
      private Button sendButton;
      @FXML
      private Button goBackButton;
      @FXML
      private TextField messageTextField;
      @FXML
      private Label replyLabel;
      @FXML
      TableView<Message> tableViewMsg;
      @FXML
      TableColumn<Message, String> friendColumn;

      public MsgController() throws SQLException {}

      public void setUser(User user) {
            this.user = user;
      }

      public void setFriend(User selected) {
            this.selected = selected;
      }

      public void setService(Service service){
            this.service = service;
            this.service.addObserver(this);
            initModel();
      }

      private void initModel() {
            Iterable<Message> messages = service.listMessagesUsers(user.getId(),selected.getId());
            List<Message> messageList = StreamSupport.stream(messages.spliterator(), false)
                    .collect(Collectors.toList());
            model.setAll(messageList);

      }

      @FXML
      public void initialize() throws Exception {
            friendColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
                  @Override
                  public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> param) {
                        return new SimpleStringProperty(param.getValue().getMessage());
                  }
            });

            tableViewMsg.setItems(model);

        }

      public void goBackOnAction(ActionEvent event){
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            stage.close();
        }


      public void sendButtonOnAction(ActionEvent event) throws SQLException {
            String msg = messageTextField.getText();
            service.sendOneMessage(user.getId(),selected.getId(),msg);
            initModel();
        }

      public void replyButtonOnAction(ActionEvent event) throws SQLException {
            Message msg = tableViewMsg.getSelectionModel().getSelectedItem();
            if (msg != null) {
                  String reply = messageTextField.getText();
                  if (reply != ""){
                        service.replyOneMessage(user.getId(),selected.getId(),reply);
                        initModel();
                  }
                  else {
                        replyLabel.setText("Nu ati scris niciun mesaj");
                  }
            } else
                  replyLabel.setText("Nu ati selectat nici un mesaj");
      }

      @Override
        public void update(MessageChangeEvent messageChangeEvent) {
            initModel();
        }


}
