package controller;

import entities.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import request.AuthRequestDTO;
import response.JwtResponseDTO;
import service.JwtService;
import service.RefreshTokenService;

public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(),
                        authRequestDto.getPassword()));
                if(authentication.isAuthenticated()){
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDto.getUsername());
                            return ResponseEntity<>(JwtResponseDTO
                            .builder()
                            .accessToken(jwtService.GenerateJwtToken(authRequestDto.getUsername()))
                            .token(refreshToken.getTokenId())
                            .build(),
                            HttpStatus.OK);

                }else{
                    return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
                }

    }
}
