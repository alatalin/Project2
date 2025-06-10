package models.response;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class GenerateTokenResponseModel {

    String  result, status, token;
    LocalDateTime expires;
}
