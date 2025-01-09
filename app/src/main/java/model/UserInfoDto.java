package model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import entities.UserInfo;
import lombok.Builder;
import lombok.Data;



@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {

    private String userName;

    private String lastName;

    private Long phoneNumber;

    private String email;
}
