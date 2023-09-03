package lk.ijse.csms.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private String employeeId;
    private String name;
    private String address;
    private String nic;
    private String telephoneNumber;
    private String rank;
}
