package movie.domain.Entity.Movie;

import movie.domain.Entity.Member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieEntity,Integer> {
    // mvid로 mvno찾아오는 착한친구
    @Query(nativeQuery = true,value = "select mvno from movie where mvid=:mvid")
    int findMvno(@Param("mvid")String mvid);
    Optional<MovieEntity> findBymvid(String  mvid);
   /* Optional<MovieEntity> findBymno(int mno);*/



}
