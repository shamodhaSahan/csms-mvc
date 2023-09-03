package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
    private String customerId;
    private String name;
    private String address;
    private String nic;
    private String telephoneNumber;
    private String email;
}