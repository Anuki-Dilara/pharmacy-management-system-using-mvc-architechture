package lk.ijse.carepoint.dto.tm;
import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class MedicineTM {
    private String medicine_id;
    private String medicine_name;
    private String medicine_details;
    private double unit_price;
    private int quantity;


    private JFXButton btn;
}
