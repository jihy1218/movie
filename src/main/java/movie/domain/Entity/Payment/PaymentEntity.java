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
    @Column(name="pmoviename")
    private String pmoviename;
    @Column(name="ptime ")
    private String ptime ;
    @Column(name="ppeople ")
    private String ppeople ;
    @Column(name="pseat ")
    private String pseat ;
    @Column(name="ptype ")
    private String ptype ;
    @Column(name="pprice ")
    private String pprice ;
    @Column(name="tno")
    private int tno;

}
