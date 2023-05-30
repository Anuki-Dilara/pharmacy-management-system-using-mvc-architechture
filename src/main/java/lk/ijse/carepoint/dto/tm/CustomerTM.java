package lk.ijse.carepoint.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class CustomerTM {
    private String customer_id;
    private String name;
    private String address;
    private int age;
    private String contactNumber;
    private JFXButton btn;
}
