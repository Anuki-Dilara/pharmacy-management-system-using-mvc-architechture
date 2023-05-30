package lk.ijse.carepoint.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.CartDTO;
import lk.ijse.carepoint.dto.Customer;
import lk.ijse.carepoint.dto.Medicine;
import lk.ijse.carepoint.dto.PlaceOrder;
import lk.ijse.carepoint.model.OrderModel;
import lk.ijse.carepoint.dto.tm.CustomerOrderTM;
import lk.ijse.carepoint.model.CustomerModel;
import lk.ijse.carepoint.model.MedicineModel;
import lk.ijse.carepoint.model.PlaceOrderModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class OrderViewFormController implements Initializable {

    public AnchorPane orderWindowA1;
    public AnchorPane orderWindowA2;
    public Button btnCustomerOrder;
    public Button btnSupplierOrder;
    public JFXButton btnAddToCart;
    public Label lblUnitPrice;
    public Label lblCustomerOrderAmount;
    public JFXTextField TxtTotal;
    public Label lblOrderId;
    public Label lblOrderDate;
    public TextField txtOrderId;
    public Button btnOrderReport;

    @FXML
    private Label lblCustomerName;

    @FXML
    private ComboBox<String> cmbMedicineId;

    @FXML
    private Label lblMedicineName;

    @FXML
    private TextField txtQuantity;

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private Label lblQuantityOnHand;

    @FXML
    private JFXButton btnCustomerPlaceOrder;

    @FXML
    private TableView<CustomerOrderTM> tblCustomerOrder;

    @FXML
    private TableColumn<?, ?> colMedicineId;

    @FXML
    private TableColumn<?, ?> colMedicineName;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colCustomerOrderAction;


    private String searchText="";
    private ObservableList<CustomerOrderTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextOrderId();
        generateOrderDate();
        loadMedicineIds();
        loadCustomerids();
        setCellValueFactory();
    }

    private void loadCustomerids() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> customerids = CustomerModel.getCustomerids();
            for (String customerid : customerids) {
                obList.add(customerid);
            }
            cmbCustomerId.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setCellValueFactory() {
        colMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicine_id"));
        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("medicine_name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("customerOrder_quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("customerOrder_unitPrice"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("customerOrder_amount"));
        colCustomerOrderAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }


    private void generateOrderDate() {
        lblOrderDate.setText(new SimpleDateFormat("20YY-MM-dd").format(new Date()));
    }

    private void loadMedicineIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> medicineids = MedicineModel.getMedicineids();

            for (String medicine_id : medicineids) {
                obList.add(medicine_id);
            }
            cmbMedicineId.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void generateNextOrderId() {
        try {
             String nextId = OrderModel.generateNextOrderId();
             txtOrderId.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void btnCustomerOrderOnAction(ActionEvent actionEvent) throws IOException {
        Stage thisStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader= new FXMLLoader(LoginViewFormController.class.getResource("/view/order-view-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        thisStage.setScene(scene);
        //thisStage.setMaximized(true);
    }

    public void btnSupplierOrderOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("/view/supplierorder-view-form.fxml"));
        orderWindowA2.getChildren().clear();
        orderWindowA2.getChildren().add(load);
    }

    @FXML//btnCustomerPlaceOrderOnAction
    void btnCustomerPlaceOrderOnAction(ActionEvent event) {
        String oid = txtOrderId.getText();
        System.out.println("Order Id"+oid);

        double total = Double.parseDouble(TxtTotal.getText());// Double.parseDouble(TxtTotal.getText());
        String date = String.valueOf(LocalDate.now());
        //System.out.println("Date :" +lblOrderDate.getText());
        String customerid = cmbCustomerId.getSelectionModel().getSelectedItem().toString();
        System.out.println("Customer :"+customerid);
        ArrayList<CartDTO> cartDetails = new ArrayList<>();
         //load all cart items' to orderDet
            //ails arrayList
        for (int i = 0; i < tblCustomerOrder.getItems().size(); i++) {
             //get each row details to (PlaceOrderTm)tm in each time and add them to the orderDetails
            CustomerOrderTM tm = obList.get(i);
            cartDetails.add(new CartDTO(tm.getMedicine_id(), tm.getMedicine_name(), tm.getCustomerOrder_quantity(), tm.getCustomerOrder_unitPrice(), tm.getCustomerOrder_amount()));
        }
        System.out.println("For Loop Done");
        PlaceOrder placeOrder = new PlaceOrder(oid,total,date,customerid, cartDetails);
        try {
            Boolean orderPlace = PlaceOrderModel.PlaceOrder(placeOrder);
            if (orderPlace){
                new Alert(Alert.AlertType.CONFIRMATION,"Order Placed",ButtonType.OK).show();
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }

    private double calAmount(double unitprice,int qty){
        double total=unitprice*qty;
        return total;
    }
    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String medicine_id = cmbMedicineId.getValue();
        String medicine_name = lblMedicineName.getText();
        int customerOrder_quantity = Integer.parseInt(txtQuantity.getText());
        double customerOrder_unitPrice = Double.parseDouble(lblUnitPrice.getText());
        //double customerOrder_amount = calAmount(customerOrder_unitPrice,customerOrder_quantity) ;
        //lblCustomerOrderAmount.setText(String.valueOf(calAmount(customerOrder_unitPrice,customerOrder_quantity)));
        double amount = Double.parseDouble(lblCustomerOrderAmount.getText());
        JFXButton btnDelete = new JFXButton("Delete");
        btnDelete.setCursor(Cursor.HAND);

        setDeleteBtnOnAction(btnDelete); /* set action to the btnRemove */

        if (!obList.isEmpty()) {
            for (int i = 0; i < tblCustomerOrder.getItems().size(); i++) {
                if (colMedicineId.getCellData(i).equals(medicine_id)) {

                    customerOrder_quantity += (int) colQuantity.getCellData(i);
                    amount = customerOrder_quantity * customerOrder_unitPrice;

                    obList.get(i).setCustomerOrder_quantity(customerOrder_quantity);
                    obList.get(i).setCustomerOrder_amount(amount);

                    tblCustomerOrder.refresh();
                    calculateNetTotal();
                    return;
                }
            }
        }

        CustomerOrderTM tm = new CustomerOrderTM(medicine_id, medicine_name, customerOrder_quantity, customerOrder_unitPrice, amount, btnDelete);
        obList.add(tm);
        tblCustomerOrder.setItems(obList);
        calculateNetTotal();

        txtQuantity.setText("");
        lblMedicineName.setText("");
        lblQuantityOnHand.setText("");
        lblCustomerName.setText("");
        txtQuantity.clear();

    }

   private void calculateNetTotal() {
        double netTotal = 0.0;
        for (int i = 0; i < tblCustomerOrder.getItems().size(); i++) {
            double total  = (double) colAmount.getCellData(i);
            netTotal += total;
        }
        lblCustomerOrderAmount.setText(String.valueOf(netTotal));
        TxtTotal.setText(String.valueOf(netTotal));
    }

    private void setDeleteBtnOnAction(Button btnRemove) {
        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = tblCustomerOrder.getSelectionModel().getSelectedIndex();
                obList.remove(index);

                tblCustomerOrder.refresh();
                //calculateNetTotal();
            }

        });
    }


    public void cmbCustomerIdOnAction(ActionEvent actionEvent) {
        String customer_id = String.valueOf(cmbCustomerId.getSelectionModel().getSelectedItem());
        try {
            Customer customer = OrderModel.searchById(customer_id);
            lblCustomerName.setText(customer.getCustomer_name());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }

    }

    public void cmbMedicineIdOnAction(ActionEvent actionEvent) {
        String medicine_id = String.valueOf(cmbMedicineId.getSelectionModel().getSelectedItem());
        try {
            Medicine medicine = OrderModel.searchByMedicineId(medicine_id);
            lblMedicineName.setText(medicine.getMedicine_name());
            lblQuantityOnHand.setText(String.valueOf(medicine.getQuantity()));
            lblUnitPrice.setText(String.valueOf(medicine.getUnit_price()));
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void txtQuantityOnAction(ActionEvent event) {
        int qty = Integer.parseInt(txtQuantity.getText());
        double price = Double.parseDouble(lblUnitPrice.getText());
        double total=qty*price;
        lblCustomerOrderAmount.setText(String.valueOf(total));
    }


}
