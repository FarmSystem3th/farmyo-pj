package farmsystem.famyo.domain.member.service;

import farmsystem.famyo.domain.member.dto.*;
import farmsystem.famyo.domain.member.entity.Member;

import java.util.Optional;

public interface MemberService {
    SignupResponseDTO initiateSignup(SignupRequestDTO signupRequestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    Optional<Member> getLoginUserInfoByMemberId(String memberId);

    Optional<JwtInfoResponseDTO> getLoginUserInfoByUserid(String name);

    String signout(String memberId);

}
