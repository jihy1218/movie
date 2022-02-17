package movie.domain.Entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer> {
}
