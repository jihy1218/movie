package movie.domain.Entity.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DateRepository extends JpaRepository<DateEntity,Integer> {
    // 날짜 찾기용
    @Query(nativeQuery = true,value = "select ddate from date where mvno=:mvno")
    List<String> findDateByMvno(@Param("mvno")int mvno);
    // 시간 찾기용
    @Query(nativeQuery = true,value = "select * from date where ddate=:ddate and mvno=:mvno")
    List<DateEntity> findTimeByDate(@Param("ddate")String day,@Param("mvno")int mvno);
}
