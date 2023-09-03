package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierTM {
    private String supplierId;
    private String name;
    private String address;
    private String telephoneNumber;
    private String email;
    private JFXButton edit;
}
