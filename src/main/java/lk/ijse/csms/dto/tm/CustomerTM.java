package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerTM {
    private String customerId;
    private String name;
    private String address;
    private String nic;
    private String telephoneNumber;
    private String email;
    private JFXButton edit;
}