package gui.misc.converters;

import data.entities.User;
import data.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTableEntry {
    private User user;

    private Integer id;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private String address;

    public UserTableEntry(User user) {
        this.id = user.getId();
        this.role = user.getRole();
        this.firstName = user.getUserInfo().getFirstName();
        this.lastName = user.getUserInfo().getLastName();
        this.phone = user.getUserInfo().getPhone();
        this.mail = user.getUserInfo().getMail();
        this.address = user.getUserInfo().getAddress();

        this.user = user;
    }

    public static ITableConverter<UserTableEntry, User> converter = UserTableEntry::new;
}


