package service;

import entities.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import repository.UserRepository;

// publishes Event to the queue and save sthe user to Database
@Component
@Data
@AllArgsConstructor
public class UserDetailsServiceImpl implements  UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username){
        UserInfo userInfo = userRepository.findByUsername(username);
        if(userInfo == null){
            throw new UsernameNotFoundException("User not found");
        }else{
            return new CustomUserDetails(userInfo);
        }
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){

        return userRepository.findByUsername(userInfoDto.getUserName());
    }

    public Boolean signUpuser(UserInfoDto userInfoDto){
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
    }
}
