package lk.ijse.csms.dto.tm;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginTM {
    private String loginId;
    private String userId;
    private String userName;
    private String dateTime;
    private String telephoneNumber;
    private String rank;
}
