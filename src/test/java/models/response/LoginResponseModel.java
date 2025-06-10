package models.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseModel {

    String userId, username, password, token;
    Boolean isActive;
    LocalDateTime expires, created_date;
}

