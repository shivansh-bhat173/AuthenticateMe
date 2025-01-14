package myapp.controller;

import myapp.entities.RefreshToken;
import myapp.model.RefreshTokenRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import myapp.request.AuthRequestDTO;
import myapp.response.JwtResponseDTO;
import myapp.service.JwtService;
import myapp.service.RefreshTokenService;

public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("myapp/auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(),
                        authRequestDto.getPassword()));
                if(authentication.isAuthenticated()){
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
                    return new ResponseEntity<>(JwtResponseDTO
                            .builder()
                            .accessToken(jwtService.GenerateJwtToken(authRequestDto.getUsername()))
                            .token(refreshToken.getTokenId())
                            .build(),HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
                }

    }
    @PostMapping("myapp/auth/v1/refreshToken")
    public JwtResponseDTO RefreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){

        return refreshTokenService.findByTokenId(refreshTokenRequestDto.getTokenId())
                .map(refreshTokenService ::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo ->{
                    String accesToken = jwtService.GenerateJwtToken(userInfo.getUsername());
                    return JwtResponseDTO
                            .builder()
                            .accessToken(refreshTokenRequestDto.getTokenId())
                            .build();
                }).orElseThrow(()->new RuntimeException("Could not find the refresh token in DB"));
    }

}
