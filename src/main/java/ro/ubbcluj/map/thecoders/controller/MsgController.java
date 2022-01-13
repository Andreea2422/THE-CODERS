package ro.ubbcluj.map.thecoders.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MsgController implements Observer<UserChangeEvent> {
      Service service;
      ObservableList<Message> model = FXCollections.observableArrayList();
      private User user;
      User selected;
      //private List<Label> messages = new ArrayList<>();

      @FXML
      private Button sendButton;
      @FXML
      private Button goBackButton;
      @FXML
      private TextField messageTextField;
      @FXML
      private VBox chatBox;
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


      public void Stuff(){
            String message = messageTextField.getText();
            //User user = repository.findOneByUsername(messageTextField.getText(), messageTextField.getText());
        }

      private int index = 0;
      public void sendButtonOnAction(ActionEvent event) throws SQLException {
            String msg = messageTextField.getText();
            Long id1 = Long.valueOf(1);
            Long id2 = Long.valueOf(2);
            service.sendOneMessage(id1,id2,msg);
//            sendButton.setOnAction(event1 -> {messages.add(new Label(msg));
//                  if(index%2==0){
//
//                        messages.get(index).setAlignment(Pos.CENTER_LEFT);
//                        System.out.println("1");
//
//                  }else{
//
//                        messages.get(index).setAlignment(Pos.CENTER_RIGHT);
//                        System.out.println("2");}
//
//                  chatBox.getChildren().add(messages.get(index));
//                  index++;
//
//
//            });
        }

      @Override
        public void update(UserChangeEvent userChangeEvent) {
            initModel();
        }


}
