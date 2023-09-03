package lk.ijse.csms.dto.tm;
import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeTM {
    private String employeeId;
    private String name;
    private String address;
    private String nic;
    private String telephoneNumber;
    private String rank;
    private JFXButton edit;
}