package farmsystem.famyo.domain.member.repository;

import farmsystem.famyo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findByMemberId(Long memberId);
}
