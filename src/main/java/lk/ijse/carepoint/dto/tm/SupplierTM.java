package lk.ijse.carepoint.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class SupplierTM {
    private String supplier_id;
    private String supplier_name;
    private String supplier_address;
    private String supplier_phoneNumber;
    private JFXButton btn;

}
