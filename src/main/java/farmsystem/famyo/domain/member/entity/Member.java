package farmsystem.famyo.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    // 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    // 이름
    @Column(nullable = false)
    private String userName;

    // 이메일
    @Column(nullable = false)
    private String email;

    // 비밀번호
    @Column(nullable = false)
    private String password;
}
