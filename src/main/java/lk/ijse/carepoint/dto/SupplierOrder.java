package lk.ijse.carepoint.dto;

import lombok.*;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class SupplierOrder {
    private String su_op_id;
    private String date;
    private double amount;
    private String supid;
    private ArrayList<Detail> details = new ArrayList<>();
}
