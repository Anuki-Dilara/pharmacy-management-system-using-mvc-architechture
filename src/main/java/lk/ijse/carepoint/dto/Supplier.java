package lk.ijse.carepoint.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Supplier {
    private String supplier_id;
    private String supplier_name;
    private String supplier_address;
    private String supplier_phoneNumber;
}
