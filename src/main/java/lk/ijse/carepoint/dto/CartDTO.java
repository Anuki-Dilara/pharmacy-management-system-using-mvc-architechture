package lk.ijse.carepoint.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CartDTO {
    private String medicine_id;
    private String medicine_name;
    private int customerOrder_quantity;
    private double customerOrder_unitPrice;
    private double customerOrder_amount;
}
