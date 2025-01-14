package myapp.service;

import myapp.entities.RefreshToken;
import myapp.entities.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import myapp.repository.RefreshTokenRepository;
import myapp.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    //create a fresh refresh token and store/replace in DB
    public RefreshToken createRefreshToken(String userName){
        // create a refreshToken and save it in the db using myapp.repository class, By the username,
        UserInfo userInfoExtracted = userRepository.findByUsername(userName);
        RefreshToken refreshToken = RefreshToken
                .builder()
                .userInfo(userInfoExtracted)
                .tokenId(UUID.randomUUID().toString())
                .expiryDate(java.time.Instant.now().plusSeconds(600000))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){

        // Verify if the refreshToken has expired
        if(refreshToken.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.deleteById(refreshToken.getId());
            throw new RuntimeException(refreshToken.getTokenId() + " Refresh token has expired!! Please login again");
        }else{
            return refreshToken;
        }
    }

    public Optional<RefreshToken> findByTokenId(String tokenId){
        return refreshTokenRepository.findByTokenId(tokenId);
    }
}
