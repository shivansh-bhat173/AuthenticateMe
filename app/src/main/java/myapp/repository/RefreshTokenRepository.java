package myapp.repository;

import myapp.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByTokenId(String token_1);
    void deleteById(Long id);

}

