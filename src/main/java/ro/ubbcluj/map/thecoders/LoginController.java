package ro.ubbcluj.map.thecoders;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ro.ubbcluj.map.thecoders.domain.User;
import ro.ubbcluj.map.thecoders.domain.validators.UserValidator;
import ro.ubbcluj.map.thecoders.domain.validators.ValidationException;
import ro.ubbcluj.map.thecoders.repository.Repository;
import ro.ubbcluj.map.thecoders.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.thecoders.repository.file.UserFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

   @FXML
   private Button cancelButton;
   @FXML
   private Label loginMessageLabel;
   @FXML
   private ImageView logoImageView;
   @FXML
   private ImageView lockImageView;
   @FXML
   private TextField usernameTextField;
   @FXML
   private PasswordField enterPasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         File logoFile = new File("Images/logo.png");
         Image logoImage = new Image(logoFile.toURI().toString());
         logoImageView.setImage(logoImage);

         File lockFile = new File("Images/lock.png");
         Image lockImage = new Image(lockFile.toURI().toString());
         lockImageView.setImage(lockImage);
    }

   public void loginButtonOnAction(ActionEvent event) throws SQLException {
       if(usernameTextField.getText().isBlank() == false && enterPasswordField.getText().isBlank() == false){
           validateLogin();
       }
       else{
           loginMessageLabel.setText("Please enter username and password");
       }
   }

   public void signupButtonOnAction(ActionEvent event){
       createAccountForm();
   }

   public void cancelButtonOnAction(ActionEvent event){
       Stage stage = (Stage) cancelButton.getScene().getWindow();
       stage.close();
   }

   public void validateLogin() throws SQLException {
   //   JavaPostgresSQL.findIntoDatabase(usernameTextField.getText(),enterPasswordField.getText());

       Repository<Long,User> repository = new UtilizatorDbRepository("jdbc:postgresql://localhost:5432/academic", "postgres","1234",new UserValidator());
      // var user = repository.findOneByUsername(usernameTextField.getText(), enterPasswordField.getText());
      var user = repository.findOne(2L);
       if(user == null){
           loginMessageLabel.setText("Invalid login. Please try again!");
       }else{
           loginMessageLabel.setText("Congratulations!");
       }

       //         DatabaseConnection connectNow = new DatabaseConnection();
//         Connection connectDB = connectNow.getConnection();
//
//         String verifyLogin = "SELECT count(1) FROM users WHERE user_name = '" + usernameTextField.getText() + "' AND password = '" + enterPasswordField.getText() +"'";
//         try{
//             Statement statement = connectDB.createStatement();
//             ResultSet queryResult = statement.executeQuery(verifyLogin);
//             while(queryResult.next()){
//                 if(queryResult.getInt(1) == 1){
//                     loginMessageLabel.setText("Congratulations!");
//                 }else{
//                     loginMessageLabel.setText("Invalid login. Pleas try again!");
//                 }
//             }
//         }catch(Exception e){
//             e.printStackTrace();
//             e.getCause();
//         }
   }

   public void createAccountForm(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(scene);
            registerStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
   }
}
