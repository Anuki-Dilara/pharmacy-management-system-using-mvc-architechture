package lk.ijse.carepoint.model;

import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.Customer;
import lk.ijse.carepoint.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public static boolean save(String Customer_id, String customer_name,String customer_address,String customer_age,String phone_number) throws SQLException {
        String sql = "INSERT INTO customer(Customer_id, customer_name,cutomer_address, customer_age,phone_number) VALUES(?, ?, ?, ?,?)";

        return CrudUtil.execute(sql, Customer_id, customer_name,customer_address, customer_age,phone_number);

    }

    public static Customer search(String Customer_id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, Customer_id);

        if(resultSet.next()) {
            String customer_id = resultSet.getString(1);
            String customer_name  = resultSet.getString(2);
            String customer_address= resultSet.getString(3);
            int customer_age = resultSet.getInt(4);
            String phone_number = resultSet.getString(5);

            return new Customer(customer_id, customer_name, customer_address, customer_age,phone_number);
        }
        return null;
    }

    public static boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET  customer_name = ?, cutomer_address = ?, customer_age  = ?, phone_number=? WHERE customer_id = ?";
        return CrudUtil.execute(sql,customer.getCustomer_name(),customer.getCustomer_address(),customer.getCustomer_age(),customer.getPhone_number(),customer.getCustomer_id());
    }

    public static boolean delete(String nic) throws SQLException {
            String sql = "DELETE FROM customer WHERE customer_id = ?";

            return CrudUtil.execute(sql,nic);
        }

    public static List<Customer> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Customer");
        List<Customer> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Customer(
                    resultSet.getString(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getInt(4),
            resultSet.getString(5)
            ));
        }
        return dataList;
        }


    public static List<String> getCustomerids() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        List<String> customerids = new ArrayList<>();

        String sql = "SELECT customer_id FROM Customer";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while(resultSet.next()) {
            customerids.add(resultSet.getString(1));
        }
        return customerids;
    }

    /*public static String generateNextCustomerId() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT custtomer_id FROM customer ORDER BY customer_id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if(resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return splitOrderId(null);
    }

    public static String splitOrderId(String currentOrderId) {
        if(currentOrderId != null) {
            String[] strings = currentOrderId.split("C0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "C0"+id;
        }
        return "O0001";
    }*/

   /* public static String generateNextCustomerId() {
        String lastOrderId=generateCustomerId();
        if(lastOrderId==null){
            return "O0001";
        }else{
            String[] split=lastOrderId.split("[O]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newOrderId=String.format("O%04d", lastDigits);
            return newOrderId;
        }
    }*/

   /* public static boolean userCheckedInDB(String customer_id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE customer_id= ?";
        ResultSet resultSet = CrudUtil.execute(sql, customer_id);
        if(resultSet.next()){
            return true;
        }
        return false;
    }*/
}


