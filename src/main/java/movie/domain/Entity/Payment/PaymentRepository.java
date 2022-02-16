package movie.domain.Entity.Payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {
    @Query(nativeQuery = true , value="select * from payment where tno = :tno")
    PaymentEntity findBytno(@Param("tno")int tno);
    @Query(nativeQuery = true,value="select * from payment where pmoviename like %:search%")
    Page<PaymentEntity> findByPmoviename(@Param("search")String search, Pageable pageable);
    @Query(nativeQuery = true,value="select * from payment where tno like %:search%")
    Page<PaymentEntity> findByTno(@Param("search")String search, Pageable pageable);
    @Query(nativeQuery = true,value="select * from payment where mid like %:search%")
    Page<PaymentEntity> findByMid(@Param("search")String search, Pageable pageable);

    @Query(nativeQuery = true , value = "SELECT *, created_date FROM payment WHERE DATE(created_date) BETWEEN :date1 AND :date2 ")
    Page<PaymentEntity> findByDate(@Param("date1")String date1 , @Param("date2")String date2, Pageable pageable);
}
