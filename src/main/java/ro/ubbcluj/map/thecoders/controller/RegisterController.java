package ro.ubbcluj.map.thecoders.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ro.ubbcluj.map.thecoders.JavaPostgresSQL;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.UserValidator;
import ro.ubbcluj.map.thecoders.repository.Repository;
import ro.ubbcluj.map.thecoders.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.thecoders.services.Service;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RegisterController implements Initializable {
    Service service;

    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button closeButton;
    @FXML
    private ImageView tickImageView;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private Label confirmPasswordLabel;

    public RegisterController() throws SQLException {
    }

    public void setService(Service serviceU) {
        service = serviceU;
        initModel();
    }

    private void initModel() {
        Iterable<User> users = service.getAll();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File tickFile = new File("Images/tick.png");
        Image tickImage = new Image(tickFile.toURI().toString());
        tickImageView.setImage(tickImage);
    }

    public void registerButtonOnAction(ActionEvent event) throws SQLException {
        if(setPasswordField.getText().equals(confirmPasswordField.getText())){
            registerUser();

            registrationMessageLabel.setText("User has been registered successfully!");
            confirmPasswordLabel.setText("Password match!");
        }
        else{
            confirmPasswordLabel.setText("Password does not match!");
        }
    }

    public void closeButtonOnAction(ActionEvent event){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        //Platform.exit(); //se inchid amadoua feresterele - aici nu am nevoie
    }

    public void registerUser() throws SQLException {

//        Repository<Long,User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
        User user = new User(firstnameTextField.getText(),
                lastnameTextField.getText(),
                usernameTextField.getText(),
                setPasswordField.getText());
        user.setId(1L);
        service.saveUserServ(user);
    }
}
