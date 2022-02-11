package movie.domain.Entity.Ticketing;

import lombok.*;
import movie.domain.Entity.BaseTimeEntity;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Member.MemberEntity;

import javax.persistence.*;

@Entity
@Table(name="ticketing")
@NoArgsConstructor@AllArgsConstructor
@Setter@Getter@ToString@Builder
public class TicketingEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tno") // 티켓 넘버
    private int tno;
    @Column(name="tseat") // 예약 좌석
    private String tseat;
    @Column(name="tage") // 예약 연령
    private String tage;
    @Column(name="tprice")  //총가격
    private String tprice;
    @ManyToOne
    @JoinColumn(name="mno")  // 회원 번호
    private MemberEntity memberEntityTicket;
    @ManyToOne
    @JoinColumn(name="dno")  // 예매 번호
    private DateEntity dateEntityTicket;
}
