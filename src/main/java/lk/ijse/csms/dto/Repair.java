package lk.ijse.csms.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Repair {
    private String repairId;
    private LocalDate receiveDate;
    private LocalDate returnDate;
    private String status;
    private String description;
    private String customerId;
}