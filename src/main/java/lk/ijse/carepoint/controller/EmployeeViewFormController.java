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
import javafx.scene.layout.AnchorPane;
import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.Customer;
import lk.ijse.carepoint.dto.Employee;
import lk.ijse.carepoint.dto.tm.CustomerTM;
import lk.ijse.carepoint.dto.tm.EmployeeTM;
import lk.ijse.carepoint.model.CustomerModel;
import lk.ijse.carepoint.model.EmployeeModel;
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

public class EmployeeViewFormController implements Initializable {
    public Button btnGetREpotEmployee;
    @FXML
    private AnchorPane employeeWindowA2;

    @FXML
    private TextField txtEmployeeId;

    @FXML
    private TextField txtEmployeeAddress;

    @FXML
    private TextField txtEmployeeContactNumber;

    @FXML
    private TextField txtEmployeeName;

    @FXML
    private TextField txtEmployeeSalary;

    @FXML
    private Button btnAddEmployee;

    @FXML
    private Button btnUpdateEmployee;

    @FXML
    private Button btnDeleteEmployee;

    @FXML
    private Button btnSearchEmployee;

    @FXML
    private Button btnSearchAllEmployee;

    @FXML
    private TextField txtEmployeeAge;

    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colEmployeeName;

    @FXML
    private TableColumn<?, ?> colEmployeeAge;

    @FXML
    private TableColumn<?, ?> colEmployeeAddress;

    @FXML
    private TableColumn<?, ?> colEmployeeSalary;

    @FXML
    private TableColumn<?, ?> colEmployeeContactNumber;

    @FXML
    private TableColumn<?, ?> colEmployeeAction;



    private String searchText="";

    private ObservableList<EmployeeTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAllStudentsToTable(searchText);
        Platform.runLater(()->txtEmployeeId.requestFocus());
        //setDeleteButtonTableOnAction();

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields(newValue); //Set data to text field of selected row data of table
            }
        });

    }
    private void setDataToTextFields(EmployeeTM employeeTM) {
        txtEmployeeId.setText(employeeTM.getEmployee_id());
        txtEmployeeName.setText(employeeTM.getEmployee_name());
        txtEmployeeAge.setText(String.valueOf(employeeTM.getEmployee_age()));
        txtEmployeeAddress.setText(employeeTM.getEmployee_address());
        txtEmployeeSalary.setText(String.valueOf(employeeTM.getEmployee_salary()));
        txtEmployeeContactNumber.setText(employeeTM.getEmployee_contactNumber());

    }

    private void getAllStudentsToTable(String searchText) {
        try {
            List<Employee> employeeList = EmployeeModel.searchAll();
            for( Employee employee : employeeList) {
                if (employee.getEmployee_name().contains(searchText) || employee.getEmployee_address().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    EmployeeTM tm=new EmployeeTM(
                            employee.getEmployee_id(),
                            employee.getEmployee_name(),
                            employee.getEmployee_age(),
                            employee.getEmployee_address(),
                            employee.getEmployee_salary(),
                            employee.getEmployee_contactNumber()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            System.out.println(e);
            //  e.printStackTrace();
         //   new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    private void setDeleteButtonTableOnAction(JFXButton btnDel) {
        btnDel.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete?", yes, no).showAndWait();

            if (buttonType.get() == yes) {
                txtEmployeeId.setText(tblEmployee.getSelectionModel().getSelectedItem().getEmployee_id());
                btnSearchEmployeeOnAction(e);
                btnDeleteEmployeeOnAction(e);
                /*try {
                    btnDeleteOnAction(e);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }*/

                tblEmployee.getItems().clear();
                getAllStudentsToTable(searchText);
            }
        });
    }

    private void setCellValueFactory() {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employee_id")); //SupplierTM class attributes names
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        colEmployeeAge.setCellValueFactory(new PropertyValueFactory<>("employee_age"));
        colEmployeeAddress.setCellValueFactory(new PropertyValueFactory<>("employee_address"));
        colEmployeeSalary.setCellValueFactory(new PropertyValueFactory<>("employee_salary"));
        colEmployeeContactNumber.setCellValueFactory(new PropertyValueFactory<>("employee_contactNumber"));
        colEmployeeAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }


    @FXML
    void btnAddEmployeeOnAction(ActionEvent event) {
        String employee_id = txtEmployeeId.getText();
        String employee_name = txtEmployeeName.getText();
        int employee_age = Integer.parseInt(txtEmployeeAge.getText());
        String employee_address = txtEmployeeAddress.getText();
        double employee_salary = Double.parseDouble(txtEmployeeSalary.getText());
        String employee_contactNumber = txtEmployeeContactNumber.getText();


        try {
            boolean isSaved = EmployeeModel.save(employee_id, employee_name,employee_age, employee_address,employee_salary,employee_contactNumber);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer saved!!!").show();
                tblEmployee.getItems().clear();
                getAllStudentsToTable(searchText);
                //getAllStudentsToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
        }
        txtEmployeeId.clear();
        txtEmployeeName.clear();
        txtEmployeeAge.clear();
        txtEmployeeAddress.clear();
        txtEmployeeSalary.clear();
        txtEmployeeContactNumber.clear();
    }

    @FXML
    void btnDeleteEmployeeOnAction(ActionEvent event) {
        String employee_id = txtEmployeeId.getText();

        try {
            boolean isDeleted = EmployeeModel.delete(employee_id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee deleted !").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
    }

    /*@FXML
    void btnSearchAllEmployeeOnAction(ActionEvent event) {
        try {
            List<Employee> employeeList = EmployeeModel.searchAll();

            if (employeeList != null) {
                for (Employee employee : employeeList) {
                    System.out.println(employee.getEmployee_id() + " - "
                            + employee.getEmployee_name() + " - " + employee.getEmployee_age() + " - " + employee.getEmployee_address()+" - " + employee.getEmployee_salary()+" - " + employee.getEmployee_contactNumber());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "no any items found!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong!").show();
        }
    }*/

    @FXML
    void btnSearchEmployeeOnAction(ActionEvent event) {
        String employee_id = txtEmployeeId.getText();
        try {
            Employee employee = EmployeeModel.search(employee_id);
            if (employee != null) {
                txtEmployeeId.setText(employee.getEmployee_id());
                txtEmployeeName.setText(employee.getEmployee_name());
                txtEmployeeAge.setText(String.valueOf(employee.getEmployee_age()));
                txtEmployeeAddress.setText(String.valueOf(employee.getEmployee_address()));
                txtEmployeeSalary.setText(String.valueOf(employee.getEmployee_salary()));
                txtEmployeeContactNumber.setText(employee.getEmployee_contactNumber());
            } else {
                new Alert(Alert.AlertType.WARNING, "no employee found :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }
    private void clearFields(){
        txtEmployeeId.clear();
        txtEmployeeName.clear();
        txtEmployeeAddress.clear();
        txtEmployeeAge.clear();
        txtEmployeeSalary.clear();
        txtEmployeeContactNumber.clear();
    }

    @FXML
    void btnUpdateEmployeeOnAction(ActionEvent event) {
        String employee_id = txtEmployeeId.getText();
        String employee_name = txtEmployeeName.getText();
        int employee_age = Integer.parseInt(txtEmployeeAge.getText());
        String employee_address = txtEmployeeAddress.getText();
        double employee_salary = Double.parseDouble(txtEmployeeSalary.getText());
        String employee_contactNumber = txtEmployeeContactNumber.getText();

        var employee = new Employee(employee_id, employee_name,employee_address, employee_contactNumber,employee_salary,employee_age);   //type inference

        try {
            boolean   isUpdated = EmployeeModel.update(employee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "huree! Employee Updated!").show();
                tblEmployee.getItems().clear();
                getAllStudentsToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, "SQL Error!").show();
        }
    }

    @FXML
    void txtEmployeeIdOnAction(ActionEvent event) {
        String employee_id= txtEmployeeId.getText();

        //txtCustomerId.requestFocus();
        if(Regex.validateEmployeeid(employee_id)){
            txtEmployeeName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }

    @FXML
    void txtEmployeeNameOnAction(ActionEvent event) {
        txtEmployeeAddress.requestFocus();
    }

    @FXML
    void txtEmployeeAddressOnAction(ActionEvent event) {
        txtEmployeeSalary.requestFocus();
    }

    @FXML
    void txtEmployeeSalaryOnAction(ActionEvent event) {
        txtEmployeeContactNumber.requestFocus();
    }


    public void txtEmployeeContactNumberOnAction(ActionEvent actionEvent) {
        String contact=txtEmployeeContactNumber.getText();
        if (Regex.validateContact(contact)){
            //  btnSaveOnAction(actionEvent);
            txtEmployeeAge.requestFocus();
        }else {
            txtEmployeeContactNumber.clear();
            new Alert(Alert.AlertType.WARNING, "No matching contact number please Input SUP format!!!").show();
}
    }

    public void txtEmployeeAgeOnAction(ActionEvent event) {
        btnAddEmployeeOnAction(event);
    }

    public void btnGetREpotEmployee(ActionEvent event) {
        Thread t1=new Thread(
                () -> {
                    String billPath = "F:\\My Final Project-1st Semester\\src\\main\\resources\\report\\EmployeeReport.jrxml";
                    String sql="select * from employee";
                    String path = FileSystems.getDefault().getPath("/reports/EmployeeReport.jrxml").toAbsolutePath().toString();
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
