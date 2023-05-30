package lk.ijse.carepoint.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lk.ijse.carepoint.model.LoginModel;
import lk.ijse.carepoint.util.Regex;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginViewFormController implements Initializable {
    @FXML
    private AnchorPane LoginWindow;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD="admin";


    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {

        Stage stage = (Stage) LoginWindow.getScene().getWindow();
        //stage.initStyle(StageStyle.UNDECORATED);

        Stage stage2 = new Stage();
        stage2.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard-view-form.fxml"))));
     //   stage2.setTitle("Dashboard");

        stage2.setMaximized(true);

        /*Thread mailThread=new Thread(()->{
            try {
                EmailController.sendMail("dilshandekumpitiya@gmail.com");
            } catch (Exception e) {
                // System.out.println("Failed to send e-mail.Network err!");
                //e.printStackTrace();
                System.out.println(e);
            }
        });

        mailThread.start();*/
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if(Regex.validateUsername(username)&&Regex.validatePassword(password)){
            try {
                boolean isUserVerified ;
                try {
                    isUserVerified = LoginModel.userCheckedInDB(username,password);
                    if (isUserVerified) {

                        new Alert(Alert.AlertType.CONFIRMATION,"Login successful!").showAndWait();
                        stage.close();
                        stage2.show();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "User Not Found in DB!!!").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }catch (ClassNotFoundException e){
                new Alert(Alert.AlertType.ERROR,"Oops something wrong!!!").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Invalid Input !").show();
            txtUsername.clear();
            txtPassword.clear();
            txtUsername.requestFocus();
            stage.show();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(()->txtUsername.requestFocus());
    }
    @FXML
    void txtUsernameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }
    @FXML
    void txtPasswordOnAction(ActionEvent event) throws IOException {
        btnLoginOnAction(event);
    }
}



