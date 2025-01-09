package controller;

import entities.RefreshToken;
import model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import response.JwtResponseDTO;
import service.JwtService;
import service.RefreshTokenService;
import service.UserDetailsServiceImpl;

public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
        try{
            Boolean isSignUped = userDetailsService.signupUser(userInfoDto);
            if(Boolean.FALSE.equals(isSignUped)){
                return new ResponseEntity<>("Already Exist",HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUserName());
            String  jwtToken = jwtService.GenerateJwtToken(userInfoDto.getUserName());
            // Send the response Body populated with the JwtResponseDto
            return new ResponseEntity<>(JwtResponseDTO
                    .builder()
                    .accessToken(jwtToken)
                    .token(refreshToken.getTokenId())
                    .build(),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
