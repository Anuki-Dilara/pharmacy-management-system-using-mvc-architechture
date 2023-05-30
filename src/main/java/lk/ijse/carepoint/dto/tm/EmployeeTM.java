package lk.ijse.carepoint.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeTM {
    private String employee_id;
    private String employee_name;
    private int employee_age;
    private String employee_address;
    private double employee_salary;
    private String employee_contactNumber;
    private JFXButton btn;
}
