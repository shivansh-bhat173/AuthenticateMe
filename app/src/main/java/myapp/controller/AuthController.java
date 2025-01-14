package myapp.controller;

import myapp.entities.RefreshToken;
import myapp.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import myapp.response.JwtResponseDTO;
import myapp.service.JwtService;
import myapp.service.RefreshTokenService;
import myapp.service.UserDetailsServiceImpl;

public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/myapp/auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UserInfoDto userInfoDto){
        try{
            Boolean isSignUped = userDetailsService.signupUser(userInfoDto);
            if(Boolean.FALSE.equals(isSignUped)){
                return new ResponseEntity<>("Already Exist",HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUserName());
            String  jwtToken = jwtService.GenerateJwtToken(userInfoDto.getUserName());
            // Send the myapp.response Body populated with the JwtResponseDto
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
