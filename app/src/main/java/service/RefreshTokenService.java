package service;

import entities.RefreshToken;
import entities.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RefreshTokenRepository;
import repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String userName){
        // create a refreshToken and save it in the db using repository class, By the username,
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
        return refreshTokenRepository.findByToken(tokenId);
    }
}
