package movie.domain.Entity.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Integer> {

    @Query(nativeQuery = true , value="select * from payment where tno = :tno")
    PaymentEntity findBytno(@Param("tno")int tno);

}
