package farmsystem.famyo.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponseDTO {
    private Long memberId;
    private String userName;
}
