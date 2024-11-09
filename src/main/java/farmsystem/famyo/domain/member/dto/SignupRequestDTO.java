package farmsystem.famyo.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequestDTO {
    private String email;
    private String userName;
    private String password;
}
