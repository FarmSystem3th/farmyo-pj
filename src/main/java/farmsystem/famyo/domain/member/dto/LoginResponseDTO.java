package farmsystem.famyo.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private Long memberId;
    private String userName;
}