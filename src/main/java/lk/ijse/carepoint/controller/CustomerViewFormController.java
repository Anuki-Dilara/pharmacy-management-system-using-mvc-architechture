package lk.ijse.carepoint.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.Customer;
import lk.ijse.carepoint.dto.tm.CustomerTM;
import lk.ijse.carepoint.model.CustomerModel;
import lk.ijse.carepoint.model.LoginModel;
import lk.ijse.carepoint.model.OrderModel;
//import lk.ijse.carepoint.util.PatternRegex;
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

public class CustomerViewFormController implements Initializable {
    public AnchorPane customerWindowA2;
    public Button btnCustomerReport;

    @FXML
    private AnchorPane customerWindow;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContactNumber;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtAge;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnSearchAll;

    @FXML
    private Button btnClear;

    @FXML
    private TableView<CustomerTM> TabaleCustomer;

    @FXML
    private TableColumn<?, ?> ColCustomerId;

    @FXML
    private TableColumn<?, ?> ColName;

    @FXML
    private TableColumn<?, ?> ColAddress;

    @FXML
    private TableColumn<?, ?> ColAge;

    @FXML
    private TableColumn<?, ?> ColContact;

    @FXML
    private TableColumn<?, ?> ColAction;


    private String searchText="";

    private ObservableList<CustomerTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAllStudentsToTable(searchText);
        Platform.runLater(()->txtCustomerId.requestFocus());
        //generateNextCustomerId();
       // setDeleteButtonTableOnAction();

        TabaleCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields(newValue); //Set data to text field of selected row data of table
            }
        });

    }
    private void setDataToTextFields(CustomerTM customerTM) {
        txtCustomerId.setText(customerTM.getCustomer_id());
        txtName.setText(customerTM.getName());
        txtAddress.setText(customerTM.getAddress());
        txtContactNumber.setText(customerTM.getContactNumber());
        txtAge.setText(String.valueOf(customerTM.getAge()));

    }

    private void getAllStudentsToTable(String searchText) {
        try {
            List<Customer> cusList = CustomerModel.searchAll();
            for( Customer customer : cusList) {
                if (customer.getCustomer_name().contains(searchText) || customer.getCustomer_address().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    CustomerTM tm=new CustomerTM(
                            customer.getCustomer_id(),
                            customer.getCustomer_name(),
                            customer.getCustomer_address(),
                            customer.getCustomer_age(),
                            customer.getPhone_number()
                            ,btnDel);

                    obList.add(tm);

                  setDeleteButtonTableOnAction(btnDel);
                }
            }
            TabaleCustomer.setItems(obList);
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
                txtCustomerId.setText(TabaleCustomer.getSelectionModel().getSelectedItem().getCustomer_id());
                btnSearchOnAction(e);
                btnDeleteOnAction(e);
                /*try {
                    btnDeleteOnAction(e);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }*/

                TabaleCustomer.getItems().clear();
                getAllStudentsToTable(searchText);
            }
        });
    }

    private void setCellValueFactory() {
        ColCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer_id")); //SupplierTM class attributes names
        ColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        ColAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        ColContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        ColAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }



    public void txtNICOnAction(ActionEvent actionEvent) {
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        String NIC = txtCustomerId.getText();
        String customer_name = txtName.getText();
        String customer_address = txtAddress.getText();
        String customer_age = txtAge.getText();
        String phone_number = txtContactNumber.getText();


        try {
            boolean isSaved = CustomerModel.save(NIC, customer_name,customer_address, customer_age,phone_number);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer saved!!!").show();
                TabaleCustomer.getItems().clear();
                getAllStudentsToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
        }
        txtCustomerId.clear();
        txtName.clear();
        txtAddress.clear();
        txtAge.clear();
        txtContactNumber.clear();
    }


    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String customer_id = txtCustomerId.getText();
        String customer_name = txtName.getText();
        String customer_address = txtAddress.getText();
        int customer_age = Integer.parseInt(txtAge.getText());
        String phone_number = txtContactNumber.getText();

        var customer = new Customer(customer_id, customer_name, customer_address, customer_age,phone_number);   //type inference

        try {
         boolean   isUpdated = CustomerModel.update(customer);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "huree! Customer Updated!").show();
                TabaleCustomer.getItems().clear();
                getAllStudentsToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, "SQL Error!").show();
        }
        txtCustomerId.clear();
        txtName.clear();
        txtAddress.clear();
        txtAge.clear();
        txtContactNumber.clear();

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String NIC = txtCustomerId.getText();

        try {
            boolean isDeleted = CustomerModel.delete(NIC);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
        txtCustomerId.clear();
        txtName.clear();
        txtAddress.clear();
        txtAge.clear();
        txtContactNumber.clear();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        String NIC = txtCustomerId.getText();
        try {
            Customer customer = CustomerModel.search(NIC);
            if (customer != null) {
                txtCustomerId.setText(customer.getCustomer_id());
                txtName.setText(customer.getCustomer_name());
                txtAddress.setText(String.valueOf(customer.getCustomer_address()));
                txtAge.setText(String.valueOf(customer.getCustomer_age()));
                txtContactNumber.setText(String.valueOf(customer.getPhone_number()));
            } else {
                new Alert(Alert.AlertType.WARNING, "no customer found :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }

    }

    /*public void btnSearchAllOnAction(ActionEvent actionEvent) {
        try {
            List<Customer> customerList = CustomerModel.searchAll();

            if (customerList != null) {
                for (Customer customer : customerList) {
                    System.out.println(customer.getCustomer_id() + " - "
                            + customer.getCustomer_name() + " - " + customer.getCustomer_address() + " - " + customer.getCustomer_age()+" - " + customer.getPhone_number());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "no any items found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong!").show();
        }
    }*/

    private void clearFields(){
        txtCustomerId.clear();
        txtName.clear();
        txtAddress.clear();
        txtAge.clear();
        txtContactNumber.clear();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void txtCustomerIdOnAction(ActionEvent event) {
       String customer_id= txtCustomerId.getText();
        if(Regex.validateCustomerid(customer_id)){
            txtName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }
    @FXML
    void txtNameOnAction(ActionEvent event) {
            txtAddress.requestFocus();
    }

    @FXML
    void txtAddressOnAction(ActionEvent event) {
            txtAge.requestFocus();
    }

    @FXML
    void txtAgeOnAction(ActionEvent event) {
        
    }
    @FXML
    void txtContactNumberOnAction(ActionEvent event) {
        String contact=txtContactNumber.getText();
        if (Regex.validateContact(contact)){
            //  btnSaveOnAction(actionEvent);
            //txtParentName.requestFocus();
        }else {
            txtContactNumber.clear();
            new Alert(Alert.AlertType.WARNING, "No matching contact number please Input SUP format!!!").show();
        }
    }


    public void txtAgeOnKeyReleased(KeyEvent keyEvent) {
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        /*if (!txtContactNumber.getText().matches(PatternRegex.getMobilePattern().pattern())) {
            txtContactNumber.setStyle("-fx-text-fill: Red;");
        } else txtContactNumber.setStyle("-fx-text-fill: Black;");*/
    }

    public void txtAddressOnKeyReleased(KeyEvent keyEvent) {
       /* if (!txtAddress.getText().matches(PatternRegex.getAddressPattern().pattern())) {
            txtAddress.setStyle("-fx-text-fill: Red;");
        } else txtAddress.setStyle("-fx-text-fill: Black;");*/
    }

    public void btnCustomerReportOnAction(ActionEvent event) {
        Thread t1=new Thread(
                () -> {
                    String billPath = "F:\\My Final Project-1st Semester\\src\\main\\resources\\report\\customerReport.jrxml";
                    String sql="select * from customer";
                    String path = FileSystems.getDefault().getPath("/reports/customerReport.jrxml").toAbsolutePath().toString();
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
}
