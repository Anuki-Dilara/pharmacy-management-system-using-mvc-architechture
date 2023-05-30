package lk.ijse.carepoint.model;

import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.*;
import lk.ijse.carepoint.dto.PlaceOrder;
import lk.ijse.carepoint.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderModel {

    public static String generateNextOrderId() throws SQLException, ClassNotFoundException {
        String lastOrderId=generateOrderId();
        if(lastOrderId==null){
            return "O0001";
        }else{
            String[] split=lastOrderId.split("[O]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newOrderId=String.format("O%04d", lastDigits);
            return newOrderId;
        }
    }

    private static String generateOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT customerOrder_id FROM customerorder ORDER BY customerOrder_id DESC LIMIT 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static Customer searchById(String cusromer_id) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, cusromer_id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

    public static Medicine searchByMedicineId(String medicine_id) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Medicine WHERE medicine_id = ?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, medicine_id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new Medicine(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }

   public static boolean save(Order o1) throws SQLException {
       String sql = "Insert Into customerorder values(?,?,?,?)";
       return CrudUtil.execute(sql,o1.getOrder_id(),o1.getOrder_amount(),o1.getOrder_date(),o1.getCustomer_id());
   }

    public static boolean saveOrderDetail(ArrayList<CartDTO> cartdetail, PlaceOrder placeOrder) throws SQLException {
        for (CartDTO cartDTO : cartdetail){
            if (!saveorder(cartDTO,placeOrder)){
                return false;
            }
        }
        return true;
    }

    private static boolean saveorder(CartDTO cartDTO, PlaceOrder placeOrder) throws SQLException {
       String sql ="Insert Into customer_order_medicine values(?,?,?)";
        return CrudUtil.execute(sql,placeOrder.getOrder_id(),cartDTO.getMedicine_id(),cartDTO.getCustomerOrder_quantity());
    }

    public static List<String> getOrderids() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        List<String> orderids = new ArrayList<>();

        String sql = "SELECT order_id FROM order";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while(resultSet.next()) {
            orderids.add(resultSet.getString(1));
        }
        return orderids;
    }

    public static HashMap<Integer, Double> getMonthlyIncome() throws SQLException {
        ResultSet rs = CrudUtil.execute("select sum(customerOrder_amount) as total, MONTH(customerOrder_date) as month from " +
                "customerorder where YEAR(customerOrder_date) = 2023  group by month");
        HashMap<Integer,Double> hm = new HashMap<>();
        while (rs.next()){
            hm.put(rs.getInt(2),rs.getDouble(1));
        }
        return hm;
    }
}
