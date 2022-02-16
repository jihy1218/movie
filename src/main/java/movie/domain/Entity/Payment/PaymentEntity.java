package movie.domain.Entity.Payment;

import lombok.*;
import movie.domain.Entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@NoArgsConstructor@AllArgsConstructor
@Setter@Getter@ToString@Builder
public class PaymentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pno")
    private int pno;
    @Column(name="pmoviename") // 예약한 영화명
    private String pmoviename;
    @Column(name="ptime ")      // 예약한 날짜
    private String ptime ;
    @Column(name="ppeople ")     // 예약한 인원
    private String ppeople ;
    @Column(name="pseat ")      // 예약한 좌석
    private String pseat ;
    @Column(name="ptype ")      // 결제완료 ,환불처리중, 환불완료
    private String ptype ;
    @Column(name="pprice ")     // 취소 금액 & 결제금액
    private String pprice ;
    @Column(name="tno")         // 예약번호
    private int tno;
    @Column(name="mid")         // 회원아이디
    private String mid;

}
