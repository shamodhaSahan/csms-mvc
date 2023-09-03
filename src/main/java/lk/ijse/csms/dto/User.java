package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String userId;
    private String userName;
    private String userPassword;
    private String nic;
    private String telephoneNumber;
    private String email;
    private String rank;
}