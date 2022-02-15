package movie.domain.Entity.Ticketing;

import movie.domain.Entity.Member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketingRepository extends JpaRepository<TicketingEntity,Integer> {

    @Query( nativeQuery = true , value = "SELECT * FROM ticketing where mno = :mno" )
    List<TicketingEntity> findticketlist(@Param("mno") int mno);

}
