package ro.ubbcluj.map.thecoders;

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

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File tickFile = new File("Images/tick.png");
        Image tickImage = new Image(tickFile.toURI().toString());
        tickImageView.setImage(tickImage);
    }

    public void registerButtonOnAction(ActionEvent event){
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

    public void registerUser(){
        JavaPostgresSQL.writeToDatabase(firstnameTextField.getText(),
                lastnameTextField.getText(),
                usernameTextField.getText(),
                setPasswordField.getText());
    }
}
