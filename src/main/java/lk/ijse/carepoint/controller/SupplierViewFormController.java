package lk.ijse.carepoint.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.carepoint.dto.Customer;
import lk.ijse.carepoint.dto.Supplier;
import lk.ijse.carepoint.dto.tm.CustomerTM;
import lk.ijse.carepoint.dto.tm.SupplierTM;
import lk.ijse.carepoint.model.CustomerModel;
import lk.ijse.carepoint.model.SupplierModel;
import lk.ijse.carepoint.util.Regex;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierViewFormController implements Initializable {

    @FXML
    private AnchorPane SupplierWindow;

    @FXML
    private TextField txtSupplierId;

    @FXML
    private TextField txtSupplierAddress;

    @FXML
    private TextField txtSupplierName;

    @FXML
    private TextField txtSupplierContactNumber;

    @FXML
    private Button btnAddSupplier;

    @FXML
    private Button btnUpdateSupplier;

    @FXML
    private Button btnDeleteSupplier;

    @FXML
    private Button btnSearchSupplier;

    @FXML
    private TableView<SupplierTM> TableSupplier;

    @FXML
    private TableColumn<?, ?> ColSupplierId;

    @FXML
    private TableColumn<?, ?> ColSupplierName;

    @FXML
    private TableColumn<?, ?> ColSupplierAddress;

    @FXML
    private TableColumn<?, ?> ColSupplierContactNumber;

    @FXML
    private TableColumn<?, ?> ColSupplierAction;

    private ObservableList<SupplierTM> obList = FXCollections.observableArrayList();

    private String searchText="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAllSuppliersToTable(searchText);

        TableSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields(newValue); //Set data to text field of selected row data of table
            }
        });
    }

    private void setDataToTextFields(SupplierTM supplierTM) {
        txtSupplierId.setText(supplierTM.getSupplier_id());
        txtSupplierName.setText(supplierTM.getSupplier_name());
        txtSupplierAddress.setText(supplierTM.getSupplier_address());
        txtSupplierContactNumber.setText(supplierTM.getSupplier_phoneNumber());
    }

    private void getAllSuppliersToTable(String searchText) {
        try {
            List<Supplier> supList = SupplierModel.searchAll();
            for( Supplier supplier : supList) {
                if (supplier.getSupplier_name().contains(searchText) || supplier.getSupplier_address().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    SupplierTM tm=new SupplierTM(
                            supplier.getSupplier_id(),
                            supplier.getSupplier_name(),
                            supplier.getSupplier_address(),
                            supplier.getSupplier_phoneNumber(),
                            btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            TableSupplier.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    private void setDeleteButtonTableOnAction(JFXButton btnDel) {
        btnDel.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete?", yes, no).showAndWait();

            if (buttonType.get() == yes) {
                txtSupplierId.setText(TableSupplier.getSelectionModel().getSelectedItem().getSupplier_id());
                btnSearchSupplierOnAction(e);
                btnDeleteSupplierOnAction(e);

                TableSupplier.getItems().clear();
                getAllSuppliersToTable(searchText);
            }
        });
    }

    private void setCellValueFactory() {
        ColSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplier_id")); //SupplierTM class attributes names
        ColSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplier_name"));
        ColSupplierAddress.setCellValueFactory(new PropertyValueFactory<>("supplier_address"));
        ColSupplierContactNumber.setCellValueFactory(new PropertyValueFactory<>("supplier_phoneNumber"));
        ColSupplierAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }



    public void btnAddSupplierOnAction(ActionEvent actionEvent) {
        String supplier_id = txtSupplierId.getText();
        String supplier_name = txtSupplierName.getText();
        String supplier_address = txtSupplierAddress.getText();
        String supplier_phoneNumber = txtSupplierContactNumber.getText();


        try {
            boolean isSaved = SupplierModel.save(supplier_id, supplier_name,supplier_address,supplier_phoneNumber);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier saved!!!").show();
                TableSupplier.getItems().clear();
                getAllSuppliersToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
        }
        txtSupplierId.clear();
        txtSupplierName.clear();
        txtSupplierAddress.clear();
        txtSupplierContactNumber.clear();
    }

    public void btnUpdateSupplierOnAction(ActionEvent actionEvent) {
        String supplier_id =txtSupplierId .getText();
        String supplier_name = txtSupplierName.getText();
        String supplier_address = txtSupplierAddress.getText();
        String supplier_phoneNumber = txtSupplierContactNumber.getText();

        var supplier = new Supplier(supplier_id, supplier_name, supplier_address, supplier_phoneNumber);   //type inference

        try {
            boolean   isUpdated = SupplierModel.update(supplier);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "huree! Supplier Updated!").show();
                TableSupplier.getItems().clear();
                getAllSuppliersToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, "SQL Error!").show();
        }
        txtSupplierId.clear();
        txtSupplierName.clear();
        txtSupplierAddress.clear();
        txtSupplierContactNumber.clear();

    }

    public void btnDeleteSupplierOnAction(ActionEvent actionEvent) {
        String supplier_id = txtSupplierId.getText();

        try {
            boolean isDeleted = SupplierModel.delete(supplier_id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier deleted !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
        txtSupplierId.clear();
        txtSupplierName.clear();
        txtSupplierAddress.clear();
        txtSupplierContactNumber.clear();
    }

    public void btnSearchSupplierOnAction(ActionEvent actionEvent) {
        String supplier_id = txtSupplierId.getText();
        try {
            Supplier supplier = SupplierModel.search(supplier_id);
            if (supplier != null) {
                txtSupplierId.setText(supplier.getSupplier_id());
                txtSupplierName.setText(supplier.getSupplier_name());
                txtSupplierAddress.setText(String.valueOf(supplier.getSupplier_address()));
                txtSupplierContactNumber.setText(String.valueOf(supplier.getSupplier_phoneNumber()));
            } else {
                new Alert(Alert.AlertType.WARNING, "no customer found :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }

    public void btnSearchAllOnAction(ActionEvent actionEvent) {
        try {
            List<Supplier> supplierList = SupplierModel.searchAll();

            if (supplierList != null) {
                for (Supplier supplier : supplierList) {
                    System.out.println(supplier.getSupplier_id() + " - "
                            + supplier.getSupplier_name() + " - " + supplier.getSupplier_address() + " - " + supplier.getSupplier_phoneNumber());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "no any items found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong!").show();
        }
    }
    @FXML
    void txtSupplierIdOnAction(ActionEvent event) {
        String supplier_id= txtSupplierId.getText();

        //txtCustomerId.requestFocus();
        if(Regex.validateSupplierid(supplier_id)){
            txtSupplierName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }


}
