package lk.ijse.carepoint.model;

import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.CartDTO;
import lk.ijse.carepoint.dto.Detail;
import lk.ijse.carepoint.dto.Employee;
import lk.ijse.carepoint.dto.Medicine;
import lk.ijse.carepoint.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicineModel {
    public static boolean save(String medicine_id, String medicine_name, String medicine_details,double unit_price,int quantity,String supplier) throws SQLException {
        String sql = "INSERT INTO medicine(medicine_id, medicine_name,medicine_details,unit_price,quantity,supplier_id) VALUES(?, ?, ?,?,?,?)";

        return CrudUtil.execute(sql, medicine_id, medicine_name,medicine_details,unit_price,quantity,supplier);

    }

    public static Medicine search(String Medicine_id) throws SQLException {
        String sql = "SELECT * FROM medicine WHERE medicine_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, Medicine_id);

        if(resultSet.next()) {
            String medicine_id = resultSet.getString(1);
            String medicine_name  = resultSet.getString(2);
            String medicine_details= resultSet.getString(3);
            double unit_price = Double.parseDouble(resultSet.getString(4));
            int quantity= Integer.parseInt(resultSet.getString(5));
            String suplierid = resultSet.getString(6);


            return new Medicine(medicine_id, medicine_name, medicine_details, unit_price,quantity,suplierid);
        }
        return null;
    }

    public static boolean update(Medicine medicine) throws SQLException {
        String sql = "UPDATE medicine SET  medicine_name = ?, medicine_details = ?, unit_price  = ?, quantity= ? ,supplier_id=? WHERE medicine_id = ?";
        return CrudUtil.execute(sql,medicine.getMedicine_name(),medicine.getDescription(),medicine.getUnit_price(),medicine.getQuantity(),medicine.getSupplierid(),medicine.getMedicine_id());

    }

    public static boolean delete(String Medicine_id) throws SQLException {
        String sql = "DELETE FROM medicine WHERE medicine_id = ?";

        return CrudUtil.execute(sql,Medicine_id);
    }

    public static List<String> getMedicineids() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        List<String> medicineids = new ArrayList<>();

        String sql = "SELECT medicine_id FROM Medicine";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while(resultSet.next()) {
            medicineids.add(resultSet.getString(1));
        }
        return medicineids;
    }

    public static List<Medicine> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM medicine");
        List<Medicine> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Medicine(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)

            ));
        }
        return dataList;
    }

    public static boolean updateQty(List<CartDTO> cartDTOList) throws SQLException {
        for (CartDTO cartDTO : cartDTOList){
            if (!updateQty(new Medicine(cartDTO.getMedicine_id(),cartDTO.getCustomerOrder_quantity()))){
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(Medicine m1) throws SQLException {
        String sql = "Update medicine set quantity = quantity-? Where medicine_id=?";
        return CrudUtil.execute(sql,m1.getQuantity(),m1.getMedicine_id());
    }

    public static Optional<Medicine> searchByPK(String Medicine_id) throws SQLException {
        String sql = "SELECT * FROM medicine WHERE medicine_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, Medicine_id);

        if(resultSet.next()) {
            String medicine_id = resultSet.getString(1);
            String medicine_name  = resultSet.getString(2);
            String medicine_details= resultSet.getString(3);
            double unit_price = Double.parseDouble(resultSet.getString(4));
            int quantity= Integer.parseInt(resultSet.getString(5));
            String supplier = resultSet.getString(6);

            return Optional.of(new Medicine(medicine_id, medicine_name, medicine_details, unit_price,quantity,supplier));
        }
        return Optional.empty();
    }

    public static List<Medicine> getMedicineidsbySupid(String id) throws SQLException {
        String sql = "select * from medicine where supplier_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql,id);
        List<Medicine> data = new ArrayList<>();
        while (resultSet.next()){
            data.add(new Medicine(
                     resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            ));
        }
        return data;
    }

    public static boolean updateLoadQty(List<Detail> details) throws SQLException {
        for (Detail detail : details){
            if (!updateLoadQty(new Medicine(detail.getMed_id(),detail.getQty()))){
                return false;
            }
        }
        return true;
    }

    private static boolean updateLoadQty(Medicine m1) throws SQLException {
        String sql = "Update medicine set quantity = quantity + ? Where medicine_id=?";
        return CrudUtil.execute(sql,m1.getQuantity(),m1.getMedicine_id());
    }
}
