package lk.ijse.carepoint.model;

import lk.ijse.carepoint.dto.Customer;
import lk.ijse.carepoint.dto.Employee;
import lk.ijse.carepoint.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public static boolean save(String employee_id, String employee_name, int employee_age, String employee_address, double employee_salary, String employee_contactNumber) throws SQLException {
        String sql = "INSERT INTO employee(employee_id, employee_name,employee_age, employee_address,employee_salary,employee_contactNumber) VALUES(?, ?, ?, ?,?,?)";

        return CrudUtil.execute(sql, employee_id, employee_name,employee_age, employee_address,employee_salary,employee_contactNumber);

    }

    public static List<Employee> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee");
        List<Employee> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getInt(6)

            ));
        }
        return dataList;
    }

    public static Employee search(String Employee_id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, Employee_id);

        if(resultSet.next()) {
            String employee_id = resultSet.getString(1);
            String employee_name  = resultSet.getString(2);
            String employee_address= resultSet.getString(3);
            String employee_contactNumber = resultSet.getString(4);


            double employee_salary=resultSet.getDouble(5);
            int employee_age = resultSet.getInt(6);


            return new Employee(employee_id, employee_name, employee_address, employee_contactNumber,employee_salary,employee_age);
        }
        return null;
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET  employee_name = ?, employee_age = ?, employee_address  = ?, employee_salary=?, employee_contactNumber=? WHERE employee_id = ?";
        return CrudUtil.execute(sql,employee.getEmployee_name(),employee.getEmployee_age(),employee.getEmployee_address(),employee.getEmployee_salary(),employee.getEmployee_contactNumber(),employee.getEmployee_id());

    }

    public static boolean delete(String Employee_id) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";

        return CrudUtil.execute(sql,Employee_id);
    }
}
