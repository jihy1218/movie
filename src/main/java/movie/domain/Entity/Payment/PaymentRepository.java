package movie.domain.Entity.Payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {
    @Query(nativeQuery = true , value="select * from payment where tno = :tno")
    PaymentEntity findBytno(@Param("tno")int tno);
    @Query(nativeQuery = true,value="select * from payment where pmoviename like %:search%")
    Page<PaymentEntity> findByPmoviename(@Param("search")String search, Pageable pageable);
    @Query(nativeQuery = true,value="select * from payment where tno like %:search%")
    Page<PaymentEntity> findByTno(@Param("search")String search, Pageable pageable);
    @Query(nativeQuery = true,value="select * from payment where mid like %:search%")
    Page<PaymentEntity> findByMid(@Param("search")String search, Pageable pageable);
    // 매출용
    @Query(nativeQuery = true,value = "SELECT * FROM payment WHERE ptype='결제완료' and DATE(created_date) BETWEEN :startday AND :endday")
    List<PaymentEntity> monthSales(@Param("startday")String startday, @Param("endday")String endday);
}
