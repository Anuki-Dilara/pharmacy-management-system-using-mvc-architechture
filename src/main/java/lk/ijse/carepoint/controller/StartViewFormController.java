package lk.ijse.carepoint.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class StartViewFormController {
    public AnchorPane StartWindow;
    public JFXButton btnStart;

    public void btnStartOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) StartWindow.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login-view-form.fxml"))));
    }
}
