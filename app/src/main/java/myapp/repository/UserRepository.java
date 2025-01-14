package myapp.repository;

import myapp.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Repository is an interface becasuse the JPA will extend classes Auto,matically
@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {

    // automatically writes query for below function
    public UserInfo findByUsername(String username);
}
