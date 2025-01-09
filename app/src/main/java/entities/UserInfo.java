package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;

    // will automaticallu create a table
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name= "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<UserRole> roles= new HashSet<>();

    @Column(name="user_name")
    private String userName;

    private String password;

}
