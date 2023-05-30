package lk.ijse.carepoint.model;

import lk.ijse.carepoint.db.DBConnection;
import lk.ijse.carepoint.dto.Order;
import lk.ijse.carepoint.dto.PlaceOrder;
import lk.ijse.carepoint.dto.SupplierOrder;

import java.sql.SQLException;

public class PlaceOrderModel {
    public static Boolean PlaceOrder(PlaceOrder p1) throws SQLException {
        try{
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean save = OrderModel.save(new Order(p1.getOrder_id(), p1.getOrder_amount(), p1.getOrder_date(), p1.getCustomer_id()));
            if (save){
                boolean saveOrderDetail= OrderModel.saveOrderDetail(p1.getOrderdetail(), p1);
                if (saveOrderDetail){
                    boolean updateQty = MedicineModel.updateQty(p1.getOrderdetail());
                    if (updateQty){
                        DBConnection.getInstance().getConnection().commit();
                        return true;
                    }
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        }finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static Boolean PlaceLoad(SupplierOrder s1) throws SQLException {
        try{
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean save = SupplierOrderModel.save(s1);
            if (save){
                System.out.println("Done 1");
                boolean saveOrderDetail = SupplierOrderModel.saveOrderDetail(s1.getDetails(), s1);
                if (saveOrderDetail){
                    System.out.println("Done 2");
                    boolean updateQty = MedicineModel.updateLoadQty(s1.getDetails());
                    if (updateQty){
                        System.out.println("Done 3");
                        DBConnection.getInstance().getConnection().commit();
                        return true;
                    }
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        }finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }
}
