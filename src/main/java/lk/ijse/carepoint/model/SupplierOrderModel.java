package lk.ijse.carepoint.model;

import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.*;
import lk.ijse.carepoint.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierOrderModel {
    public static Supplier searchById(String supplier_id) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Supplier WHERE supplier_id = ?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, supplier_id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );

        }
        return null;
    }

    public static boolean save(SupplierOrder o1) throws SQLException {
        String sql = "Insert Into supplierorder values(?,?,?,?)";
        return CrudUtil.execute(sql,o1.getSu_op_id(),o1.getDate(),o1.getAmount(),o1.getSupid());
    }

    public static boolean saveOrderDetail(ArrayList<Detail> details, SupplierOrder s1) throws SQLException {
        for (Detail detail : details){
            if (!saveorder(detail,s1)){
                return false;
            }
        }
        return true;
    }

    private static boolean saveorder(Detail detail, SupplierOrder supplierOrder) throws SQLException {
        String sql ="Insert Into supplier_order_medicine values(?,?,?,?)";
        return CrudUtil.execute(sql,supplierOrder.getSu_op_id(),detail.getMed_id(),detail.getQty(),detail.getAmount());
    }

    public static String generateNextLoadId() throws SQLException, ClassNotFoundException {
        String lastOrderId=generateLoadId();
        if(lastOrderId==null){
            return "L0001";
        }else{
            String[] split=lastOrderId.split("[L]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newOrderId=String.format("L%04d", lastDigits);
            return newOrderId;
        }
    }

    private static String generateLoadId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT supplierOrder_id FROM supplierorder ORDER BY supplierOrder_id DESC LIMIT 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}
