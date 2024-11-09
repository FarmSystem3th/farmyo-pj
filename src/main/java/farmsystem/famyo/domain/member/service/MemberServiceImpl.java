package farmsystem.famyo.domain.member.service;

import farmsystem.famyo.domain.member.dto.*;
import farmsystem.famyo.domain.member.entity.Member;
import farmsystem.famyo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public SignupResponseDTO initiateSignup(SignupRequestDTO signupRequestDTO) {
        Optional<Member> checkDuplicate = memberRepository.findMemberByEmail(signupRequestDTO.getEmail());
        if (checkDuplicate.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }

        Member member = Member.builder()
                .email(signupRequestDTO.getEmail())
                .userName(signupRequestDTO.getUserName())
                .password(toSHA256(signupRequestDTO.getPassword()))
                .build();
        memberRepository.save(member);

        return SignupResponseDTO.builder()
                .memberId(member.getMemberId())
                .userName(member.getUserName())
                .build();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<Member> optionalMember = memberRepository.findMemberByEmail(loginRequestDTO.getEmail());
        if (!optionalMember.isPresent()) {
            throw new IllegalArgumentException("유효한 이메일이 아닙니다.");
        }

        Member member = optionalMember.get();
        String encode_password = toSHA256(loginRequestDTO.getPassword());

        if (encode_password.equals(member.getPassword())) {
            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                    .memberId(member.getMemberId())
                    .userName(member.getUserName())
                    .build();
            return loginResponseDTO;
        }
        throw new IllegalArgumentException("비밀번호가 틀립니다.");
    }

    // Jwt Token에서 추출한 loginId로 Member 찾아오기
    @Override
    public Optional<Member> getLoginUserInfoByMemberId(String memberId) {
        return memberRepository.findByMemberId(Long.valueOf(memberId));
    }
    // 로그인한 Member 조회
    @Override
    public Optional<JwtInfoResponseDTO> getLoginUserInfoByUserid(String memberId) {
        return memberRepository.findByMemberId(Long.valueOf(memberId)).map(member ->
                JwtInfoResponseDTO.builder()
                        .memberId(member.getMemberId())
                        .userName(member.getUserName())
                        .build());
    }

    @Override
    public String signout(String memberId) {
        Member member = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(() -> new IllegalArgumentException("유효한 사용자 ID가 아닙니다."));

        memberRepository.delete(member);

        return "회원 탈퇴에 성공하였습니다.";
    }

    private String toSHA256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
