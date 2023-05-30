package lk.ijse.carepoint.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerOrderTM {
    private String medicine_id;
    private String medicine_name;
    private int customerOrder_quantity;
    private double customerOrder_unitPrice;
    private double customerOrder_amount;
    private Button btn;

}
