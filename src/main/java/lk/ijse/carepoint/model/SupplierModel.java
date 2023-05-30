package lk.ijse.carepoint.model;

import javafx.scene.control.TextField;
import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.Customer;
import lk.ijse.carepoint.dto.Supplier;
import lk.ijse.carepoint.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {


    public static boolean save(String supplier_id, String supplier_name, String supplier_address, String supplier_phoneNumber) throws SQLException {
        String sql = "INSERT INTO supplier(supplier_id, supplier_name,supplier_address, supplier_phoneNumber) VALUES(?, ?, ?,?)";

        return CrudUtil.execute(sql, supplier_id, supplier_name,supplier_address, supplier_phoneNumber);
    }

    public static Supplier search(String Supplier_id) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE supplier_id  = ?";
        ResultSet resultSet = CrudUtil.execute(sql, Supplier_id);

        if(resultSet.next()) {
            String supplier_id = resultSet.getString(1);
            String supplier_name  = resultSet.getString(2);
            String supplier_address= resultSet.getString(3);
            String supplier_phoneNumber = resultSet.getString(4);

            return new Supplier(supplier_id,supplier_name,supplier_address,supplier_phoneNumber);
        }
        return null;
    }

    public static boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE supplier SET  supplier_name = ?, supplier_address = ?, supplier_phoneNumber=? WHERE supplier_id = ?";
        return CrudUtil.execute(sql,supplier.getSupplier_name(),supplier.getSupplier_address(),supplier.getSupplier_phoneNumber(),supplier.getSupplier_id());
    }

    public static boolean delete(String Supplier_id) throws SQLException {
        String sql = "DELETE FROM supplier WHERE supplier_id = ?";

        return CrudUtil.execute(sql,Supplier_id);
    }


    public static List<Supplier> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Supplier");
        List<Supplier> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)

            ));
        }
        return dataList;
    }


    public static List<String> getSupplierids() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        List<String> supplierids = new ArrayList<>();

        String sql = "SELECT supplier_id FROM Supplier";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while(resultSet.next()) {
            supplierids.add(resultSet.getString(1));
        }
        return supplierids;
    }

}

