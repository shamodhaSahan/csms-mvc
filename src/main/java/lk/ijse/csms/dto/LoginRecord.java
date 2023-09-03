package lk.ijse.csms.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginRecord {
    private String loginId;
    private LocalDate date;
    private LocalTime time;
    private String userId;
}
