package movie.domain.Entity.Date;

import movie.domain.Entity.Payment.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(nativeQuery = true,value="select * from date where ddate like %:search%")
    Page<DateEntity> findByddate(@Param("search")String search, Pageable pageable);

    @Query(nativeQuery = true,value="select * from date where dno like %:search% ")
    Page<DateEntity> findBydno(@Param("search")String search, Pageable pageable);

    @Query(nativeQuery = true,value="select * from date where mvno like % :search% ")
    Page<DateEntity> findBymvno(@Param("search")int search, Pageable pageable);
}
