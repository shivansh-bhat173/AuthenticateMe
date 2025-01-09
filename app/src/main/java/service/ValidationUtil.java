package service;

import lombok.Builder;
import lombok.Data;
import model.UserInfoDto;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
@Builder
public class ValidationUtil {



    public static Boolean validateUser(UserInfoDto userInfoDto){
       if(!isPasswordValid(userInfoDto.getPassword())){
            return false;
        }if(!validateEmail(userInfoDto.getEmail())){
            return false;
        }
        return true;
    }

    // Check if the Password has min 8 characters length and then an uppercase and symbol
    public static Boolean isPasswordValid(String password){
        if(password.length()<8){
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
    public static boolean validateEmail(String email) {
        // Regular expression for validating email
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);
        // Create matcher object
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}
