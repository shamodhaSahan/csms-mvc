package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RepairTM {
    private String repairId;
    private LocalDate receiveDate;
    private LocalDate returnDate;
    private String state;
    private String description;
    private String customerId;
    private JFXButton edit;
}
