package movie.domain.Entity.Cnema;

import lombok.*;
import movie.domain.Entity.BaseTimeEntity;
import movie.domain.Entity.Date.DateEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cnema")
@AllArgsConstructor@NoArgsConstructor
@Setter@Getter@Builder@ToString
public class CnemaEntity  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cno")  // 영화관 번호
    private int cno;
    @Column(name="cname") // 관 이름
    private String cname;
    @Column(name="cactive") // 좌석 활성화 여부
    private String cactive;
    @Column(name="ctype")   // IMAX, 3D, 일반 등등
    private String ctype;
    @Column(name="cprice")  // 영화 가격
    private String cprice;
    @OneToMany(mappedBy = "cnemaEntityDate",cascade = CascadeType.ALL) // 영화 시간 날짜 리스트
    private List<DateEntity> dateEntityList = new ArrayList<>();
    
}
