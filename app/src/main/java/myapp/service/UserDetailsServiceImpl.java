package myapp.service;

import myapp.entities.UserInfo;
import myapp.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import myapp.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import myapp.repository.UserRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

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
    public Boolean signupUser(UserInfoDto userInfoDto){
        /// Define a function to check if username, password is correct
        if(checkIfSameUserNameExists(userInfoDto.getUserName())){
            throw new RuntimeException("UserName exists please try a different one");
        }
        if(ValidationUtil.validateUser(userInfoDto)){
            throw new RuntimeException("Please Enter Correct Format of Email or Password");
        }
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
            return false;
        }
        String userId  = UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId,userInfoDto.getUserName(), userInfoDto.getPassword(), new HashSet<UserRole>()));
        return true;
    }
    public  Boolean checkIfSameUserNameExists(String userName){
        // If a username has been taken by someone in the Database
        if(userRepository.findByUsername(userName)==null){
            return false;
        }else{
            return true;
        }
    }


}
