package movie.domain.Entity.Ticketing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketingRepository extends JpaRepository<TicketingEntity,Integer> {


}
