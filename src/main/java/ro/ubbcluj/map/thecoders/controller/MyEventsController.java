package ro.ubbcluj.map.thecoders.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import ro.ubbcluj.map.thecoders.domain.Events;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.services.Service;
import ro.ubbcluj.map.thecoders.utils.events.UserChangeEvent;
import ro.ubbcluj.map.thecoders.utils.observer.Observer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MyEventsController {
        Service service;
        ObservableList<Events> model = FXCollections.observableArrayList();
        private User user;

        @FXML
        private Button returnButton;
        @FXML
        TableView<Events> tableView;
        @FXML
        TableColumn<Events, String> tableColumnName;
        @FXML
        TableColumn<Events, String> columnDate;


        public void setService(Service service){
            this.service = service;
            initModel();
         }

        public void setUser(User user) {
            this.user = user;
         }


        @FXML
        public void initialize() throws Exception {
        tableColumnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Events, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Events, String> param) {
                return new SimpleStringProperty(param.getValue().getName());
            }
        });

//        columnDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Events, Date>, ObservableValue<Date>>() {
//            @Override
//            public ObservableValue<Date> call(TableColumn.CellDataFeatures<Events, Date> param) {
//                return new SimpleDateFormat(param.getValue().getData());
//            }
//        });
            //columnDate.setCellValueFactory(cellData -> (ObservableValue<String>) cellData.getValue().getData());

        tableView.setItems(model);

    }


        private void initModel() {
        Iterable<Events> events = service.getAllEvents(user.getId());
        List<Events> eventsList = StreamSupport.stream(events.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(eventsList);
    }



        public void returnButtonOnAction(ActionEvent event){
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }



}
