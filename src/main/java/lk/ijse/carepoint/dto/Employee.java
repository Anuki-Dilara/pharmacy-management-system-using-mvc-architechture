package lk.ijse.carepoint.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Employee {
    private String employee_id;
    private String employee_name;
    private String employee_address;
    private String employee_contactNumber;

    private double employee_salary;

    private int employee_age;



}
