package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supplier {
    private String supplierId;
    private String name;
    private String address;
    private String telephoneNumber;
    private String email;
}