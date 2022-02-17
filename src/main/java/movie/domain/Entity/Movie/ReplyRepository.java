package movie.domain.Entity.Movie;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<ReplyEntity,Integer> {

    //mvno 로 rno찾는것
    @Query(nativeQuery= true,value = "select * from reply where mvno=:mvno")
    Page<ReplyEntity> findRno(@Param("mvno")String mvno, Pageable pageable);

}
