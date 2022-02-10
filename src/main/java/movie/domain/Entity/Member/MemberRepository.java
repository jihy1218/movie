package movie.domain.Entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {

    //엔티티 검색 findby필드명
    Optional<MemberEntity> findBymno(int mno);
    Optional<MemberEntity> findBympassword(String mpassword);
    Optional<MemberEntity> findBymid(String mid);
    Optional<MemberEntity> findBymemail(String email);

}
