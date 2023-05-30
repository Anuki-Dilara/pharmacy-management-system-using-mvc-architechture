package lk.ijse.carepoint.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carepoint.model.OrderModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class DashboardViewFormController implements Initializable {
    public AnchorPane DashboardWindowA1;
    public AnchorPane DashboardWindowA2;
    public JFXButton btnEmployee;
    public JFXButton btnCustomers;
    public JFXButton btnMedicine;
    public JFXButton btnOrders;
    public JFXButton btnSupplier;
    public AnchorPane DashboardWindowA3;
    public JFXButton btnDashboard;
    public JFXButton btnLogout;
    public LineChart mainChart;
    @FXML
    private Label lblDate;

    public void initialize(){
        try {
            setMainChart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMainChart() throws SQLException {
        HashMap<Integer, Double> monthlyIncome = OrderModel.getMonthlyIncome();//studentRoomService.getMonthlyIncome();
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName(String.valueOf(LocalDate.now().getYear()));

        for(int i = 0; i <12 ; i++){
            String ar = Month.of(i + 1).getDisplayName(TextStyle.FULL, new Locale("en"));
            Double aDouble = monthlyIncome.get(i+1);
            if(aDouble!=null){
                dataSeries1.getData().add(new XYChart.Data( ar, aDouble));
            }else {
                dataSeries1.getData().add(new XYChart.Data( ar, 0.0));
            }
        }

        /*dataSeries1.getData().add(new XYChart.Data( "1", 567));
        dataSeries1.getData().add(new XYChart.Data( "2", 612));
        dataSeries1.getData().add(new XYChart.Data("3", 800));
        dataSeries1.getData().add(new XYChart.Data("4", 780));
        dataSeries1.getData().add(new XYChart.Data("5", 810));
        dataSeries1.getData().add(new XYChart.Data("6", 850));*/

        mainChart.getData().add(dataSeries1);

    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/employee-view-form.fxml"));
        DashboardWindowA3.getChildren().clear();
        DashboardWindowA3.getChildren().add(load);
    }

    public void btnCustomersOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/customer-view-form.fxml"));
        DashboardWindowA3.getChildren().clear();
        DashboardWindowA3.getChildren().add(load);
    }

    public void btnMedicineOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/medicine-view-form.fxml"));
        DashboardWindowA3.getChildren().clear();
        DashboardWindowA3.getChildren().add(load);
    }

    public void btnOrdersOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/order-view-form.fxml"));
        DashboardWindowA3.getChildren().clear();
        DashboardWindowA3.getChildren().add(load);
    }

    public void btnSupplierOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/supplier-view-form.fxml"));
        DashboardWindowA3.getChildren().clear();
        DashboardWindowA3.getChildren().add(load);
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        Stage thisStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader= new FXMLLoader(LoginViewFormController.class.getResource("/view/dashboard-view-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        thisStage.setScene(scene);
        thisStage.setMaximized(true);
    }

    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) DashboardWindowA1.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login-view-form.fxml"))));

        stage.centerOnScreen();
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateOrderDate();
    }

    private void generateOrderDate() {lblDate.setText(String.valueOf(LocalDate.now()));

    }
}
