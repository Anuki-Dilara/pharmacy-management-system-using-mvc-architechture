package lk.ijse.carepoint.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString


public class Customer {
    private String Customer_id;
    private String customer_name;
    private String customer_address;
    private int customer_age;
    private String phone_number;

}
