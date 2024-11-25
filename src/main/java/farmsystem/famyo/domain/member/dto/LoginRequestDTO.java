package farmsystem.famyo.domain.member.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String recaptchaToken;
    private String email;
    private String password;
}
