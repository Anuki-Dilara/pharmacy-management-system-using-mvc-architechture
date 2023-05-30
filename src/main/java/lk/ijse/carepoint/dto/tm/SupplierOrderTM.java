package lk.ijse.carepoint.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierOrderTM {
    private String medicine_id;
    private int supplierOrder_quantity;
    private double supplierOrder_amount;
}
