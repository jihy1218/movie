package movie.domain.Entity.Date;

import lombok.*;
import movie.domain.Entity.BaseTimeEntity;
import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Ticketing.TicketingEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="date")
@NoArgsConstructor@AllArgsConstructor
@Setter@Getter@ToString@Builder
public class DateEntity  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="dno")  // 날짜 번호
    private int dno;
    @Column(name="ddate")  // 날짜
    private String ddate;
    @Column(name="dtime")  // 시간
    private String dtime;
    @ManyToOne
    @JoinColumn(name="cno")  // 영화관 번호
    private CnemaEntity cnemaEntityDate;
    @ManyToOne
    @JoinColumn(name="mvno") // 영화 번호
    private MovieEntity movieEntityDate;
    @OneToMany(mappedBy = "dateEntityTicket",cascade = CascadeType.ALL) // 티켓 예약 리스트
    private List<TicketingEntity> ticketingEntities = new ArrayList<>();


}
