package lk.ijse.csms.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReturnedRepairTM {
    private String repairId;
    private String transactionId;
    private String description;
    private String customerId;
    private Double price;
    private LocalDate receiveDate;
    private LocalDate trueReturnDate;
}
