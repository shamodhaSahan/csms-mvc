package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RepairCartTM {
    private String repairId;
    private String description;
    private double repairPrice;
    private JFXButton edit;
}
