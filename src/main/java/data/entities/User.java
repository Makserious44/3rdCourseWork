package data.entities;

import data.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable, Requestable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private Integer password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
//    @Convert(converter = UserRoleConverter.class)
    private UserRole role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "info_id", nullable = false)
    private UserInfo userInfo;
}
