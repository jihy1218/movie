package movie.domain.Entity.Payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {
    @Query(nativeQuery = true,value="select * from payment where pmoviename like %:search%")
    Page<PaymentEntity> findByPmoviename(@Param("search")String search, Pageable pageable);
    @Query(nativeQuery = true,value="select * from payment where tno like %:search%")
    Page<PaymentEntity> findByTno(@Param("search")String search, Pageable pageable);
    @Query(nativeQuery = true,value="select * from payment where mid like %:search%")
    Page<PaymentEntity> findByMid(@Param("search")String search, Pageable pageable);
}
