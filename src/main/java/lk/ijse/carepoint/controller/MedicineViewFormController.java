package lk.ijse.carepoint.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.Employee;
import lk.ijse.carepoint.dto.Medicine;
import lk.ijse.carepoint.dto.Supplier;
import lk.ijse.carepoint.dto.tm.CustomerTM;
import lk.ijse.carepoint.dto.tm.MedicineTM;
import lk.ijse.carepoint.dto.tm.SupplierTM;
import lk.ijse.carepoint.model.CustomerModel;
import lk.ijse.carepoint.model.EmployeeModel;
import lk.ijse.carepoint.model.MedicineModel;
import lk.ijse.carepoint.model.SupplierModel;
import lk.ijse.carepoint.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MedicineViewFormController implements Initializable {

    public TextField txtSupplier;
    public Button btnReport;
    @FXML
    private Button btnAddMedicine;

    @FXML
    private Button btnUpdateMedicine;

    @FXML
    private Button btnDeleteMedicine;

    @FXML
    private Button btnSearchMedicine;

    @FXML
    private TextField txtMedicineId;

    @FXML
    private TextField txtMedicineName;

    @FXML
    private JFXTextArea txaDetails;

    @FXML
    private TextField txtMedicineQuantity;

    @FXML
    private TextField txtMedicineUnitPrice;

    @FXML
    private TableView<MedicineTM> tblMedicine;

    @FXML
    private TableColumn<?, ?> colMedicineId;

    @FXML
    private TableColumn<?, ?> colMedicineName;

    @FXML
    private TableColumn<?, ?> colMedicineUnitPrice;

    @FXML
    private TableColumn<?, ?> colMedicineQuantity;

    @FXML
    private TableColumn<?, ?> colMedicineDescription;

    @FXML
    private TableColumn<?, ?> colMedicineQuantityOnHand;

    @FXML
    private TableColumn<?, ?> colMedicineAction;

    @FXML
    private ComboBox<String> cmbSupplier;


    private ObservableList<MedicineTM> obList = FXCollections.observableArrayList();

    private String searchText="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadSupplierids();
        getAllSuppliersToTable(searchText);

        tblMedicine.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields(newValue); //Set data to text field of selected row data of table
            }
        });

    }

    private void loadSupplierids() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> supplierids = SupplierModel.getSupplierids();

            for (String supplier_id: supplierids) {
                obList.add(supplier_id);
            }
            cmbSupplier.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void setDataToTextFields(MedicineTM medicineTM) {
        txtMedicineId.setText(medicineTM.getMedicine_id());
        txtMedicineName.setText(medicineTM.getMedicine_name());
        txaDetails.setText(medicineTM.getMedicine_details());
        txtMedicineUnitPrice.setText(String.valueOf(medicineTM.getUnit_price()));
        txtMedicineQuantity.setText(String.valueOf(medicineTM.getQuantity()));

    }


    private void getAllSuppliersToTable(String searchText) {
        try {
            List<Medicine> supList = MedicineModel.searchAll();
            for( Medicine medicine : supList) {
                if (medicine.getMedicine_name().contains(searchText) || medicine.getMedicine_id().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    MedicineTM tm=new MedicineTM(
                            medicine.getMedicine_id(),
                            medicine.getMedicine_name(),
                            medicine.getDescription(),
                            medicine.getUnit_price(),
                            medicine.getQuantity(),
                            btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblMedicine.setItems(obList);
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
                txtMedicineId.setText(tblMedicine.getSelectionModel().getSelectedItem().getMedicine_id());
                btnSearchMedicineOnAction(e);
                btnDeleteMedicineOnAction(e);

                tblMedicine.getItems().clear();
                getAllSuppliersToTable(searchText);
            }
        });
    }

    private void setCellValueFactory() {
        colMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicine_id")); //SupplierTM class attributes names
        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("medicine_name"));
        colMedicineDescription.setCellValueFactory(new PropertyValueFactory<>("medicine_details"));
        colMedicineUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colMedicineQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        //colMedicineQuantityOnHand.setCellValueFactory(new PropertyValueFactory<>("medicine_quantityOnHand"));
        colMedicineAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }


    @FXML
    void btnAddMedicineOnAction(ActionEvent event) {
        String medicine_id = txtMedicineId.getText();
        String medicine_name = txtMedicineName.getText();
        String description = txaDetails.getText();
        double unit_price= Double.parseDouble(txtMedicineUnitPrice.getText());
        int quantity= Integer.parseInt(txtMedicineQuantity.getText());
        String supplier = cmbSupplier.getValue();

        try {
            boolean isSaved = MedicineModel.save(medicine_id,medicine_name,description,unit_price,quantity,supplier);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier saved!!!").show();
                tblMedicine.getItems().clear();
                getAllSuppliersToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
        }
    }

    @FXML
    void btnDeleteMedicineOnAction(ActionEvent event) {
        String medicine_id = txtMedicineId.getText();

        try {
            boolean isDeleted = MedicineModel.delete(medicine_id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Medicine deleted !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
        txtMedicineId.clear();
        txtMedicineName.clear();
        txtMedicineQuantity.clear();
        txaDetails.clear();
    }

    @FXML
    void btnSearchMedicineOnAction(ActionEvent event) {
        String medicine_id = txtMedicineId.getText();
        try {
            Medicine medicine = MedicineModel.search(medicine_id);
            if (medicine != null) {
                txtMedicineId.setText(medicine.getMedicine_id());
                txtMedicineName.setText(medicine.getMedicine_name());
                txaDetails.setText(String.valueOf(medicine.getDescription()));
                txtMedicineUnitPrice.setText(String.valueOf(medicine.getUnit_price()));
                txtMedicineQuantity.setText(String.valueOf(medicine.getQuantity()));
            } else {
                new Alert(Alert.AlertType.WARNING, "no medicine found :(").show();
            }
        } catch (SQLException e) {
            System.out.println(e);
            //new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }

    @FXML
    void btnUpdateMedicineOnAction(ActionEvent event) {
        String medicine_id = txtMedicineId.getText();
        String medicine_name = txtMedicineName.getText();
        String description = txaDetails.getText();
        double unit_price= Double.parseDouble(txtMedicineUnitPrice.getText());
        int quantity= Integer.parseInt(txtMedicineQuantity.getText());
        String supplier = cmbSupplier.getValue();

        Medicine medicine = new Medicine(medicine_id, medicine_name,description, unit_price,quantity,supplier);   //type inference

        try {
            boolean   isUpdated = MedicineModel.update(medicine);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "huree! Medicine Updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, "SQL Error!").show();
        }
    }

    @FXML
    void txtMedicineIdOnAction(ActionEvent event) {
        String medicine_id= txtMedicineId.getText();

        //txtCustomerId.requestFocus();
        if(Regex.validateMedicineid(medicine_id)){
            txtMedicineName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }

    public void tblMedicineOnMouseClicked(MouseEvent mouseEvent) {
        MedicineTM medicineTM = (MedicineTM) tblMedicine.getSelectionModel().getSelectedItem();
        try {
            Medicine medicine = MedicineModel.search(medicineTM.getMedicine_id());
            txtMedicineName.setText(medicine.getMedicine_name());
            txtMedicineUnitPrice.setText(String.valueOf(medicine.getUnit_price()));
            txtSupplier.setText(medicine.getSupplierid());
            txaDetails.setText(medicine.getDescription());
            txtMedicineQuantity.setText(String.valueOf(medicine.getQuantity()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void txtMedicineNameOnAction(ActionEvent event) {
        txtMedicineUnitPrice.requestFocus();
    }

    @FXML
    void txtMedicineQuantityOnAction(ActionEvent event) {
        txaDetails.requestFocus();
    }

    @FXML
    void txaDetailsOnAction(MouseEvent event) {
        cmbSupplier.requestFocus();
    }


    public void btnReportOnAction(ActionEvent actionEvent) {
        Thread t1=new Thread(
                () -> {
                    String billPath = "F:\\My Final Project-1st Semester\\src\\main\\resources\\report\\MedicineReport.jrxml";
                    String sql="select * from medicine";
                    String path = FileSystems.getDefault().getPath("/reports/MedicineReport.jrxml").toAbsolutePath().toString();
                    JasperDesign jasdi = null;
                    try {
                        jasdi = JRXmlLoader.load(billPath);
                        JRDesignQuery newQuery = new JRDesignQuery();
                        newQuery.setText(sql);
                        jasdi.setQuery(newQuery);
                        JasperReport js = JasperCompileManager.compileReport(jasdi);
                        JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                        JasperViewer viewer = new JasperViewer(jp, false);
                        viewer.show();
                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }

                });

        t1.start();
    }

    public void cmbSupplierOnAction(ActionEvent event) {
        btnAddMedicineOnAction(event);
    }
}
