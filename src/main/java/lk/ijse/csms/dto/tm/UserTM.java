package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserTM {
    private String userId;
    private String userName;
    private String userPassword;
    private String nic;
    private String telephoneNumber;
    private String email;
    private String rank;
    private JFXButton edit;
}
