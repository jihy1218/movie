package movie.domain.Entity.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Integer> {

    //엔티티 검색 findby필드명
    Optional<MemberEntity> findBymid(String mid);
    Optional<MemberEntity> findBymemail(String email);

    @Query( nativeQuery = true , value = "select * from member where mno like %:search% " )
    Page<MemberEntity> findAllmno(@Param("search") String search, Pageable pageable);

    @Query( nativeQuery = true , value = "select * from member where mid like %:search% " )
    Page<MemberEntity> findAllmid(@Param("search") String search, Pageable pageable);

    @Query( nativeQuery = true , value = "select *from member where mname like %:search%" )
    Page<MemberEntity> findAllmname(@Param("search") String search, Pageable pageable);
}
