package data.entities;

import data.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String login;
    private Integer password;
    private UserRole role = UserRole.client;
    private UserInfo userInfo;
}
