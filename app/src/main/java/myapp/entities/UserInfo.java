package myapp.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
@Table(name="users")
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String username;

    private String password;
    // will automaticallu create a table
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name= "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<UserRole> roles= new HashSet<>();

}
