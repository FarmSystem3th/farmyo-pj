package farmsystem.famyo.domain.member.controller;

import farmsystem.famyo.config.JwtTokenUtil;
import farmsystem.famyo.domain.member.dto.*;
import farmsystem.famyo.domain.member.entity.Response;
import farmsystem.famyo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public Response<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            SignupResponseDTO signupResponseDTO = memberService.initiateSignup(signupRequestDTO);
            return Response.success(signupResponseDTO);
        } catch (Exception e) {
            return Response.failure(e);
        }
    }

    @PostMapping("/login")
    public Response<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO loginResponseDTO = memberService.login(loginRequestDTO);

            if (loginResponseDTO == null) {
                return Response.failure("아이디 또는 비밀번호가 불일치합니다.");
            }

            long expireTimeMs = 1000 * 60 * 60;     // Token 유효 시간 = 60분

            String jwtToken = JwtTokenUtil.createToken(loginResponseDTO.getMemberId().toString(), expireTimeMs);

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", jwtToken);

            return Response.success(tokenMap);
        } catch (Exception e) {
            return Response.failure(e);
        }
    }

    @GetMapping("/jwtInfo")
    @ResponseBody
    public Response<?> userInfo(Authentication auth) {
        Optional<JwtInfoResponseDTO> jwtInfoResponseDTO = memberService.getLoginUserInfoByUserid(auth.getName());

        if (jwtInfoResponseDTO.isPresent()) {
            return Response.success(jwtInfoResponseDTO.get());
        }
        return Response.failure("사용자가 없습니다.");
    }

    @PostMapping("/logout")
    public Response<?> logout() {
        // 클라이언트 측에서 토큰 삭제
        return Response.success("로그아웃 되었습니다.");
    }

    @DeleteMapping("/signout")
    public Response<?> signout(Authentication auth) {
        try {
            return Response.success(memberService.signout(auth.getName()));
        } catch (Exception e) {
            return Response.failure(e);
        }
    }
}
