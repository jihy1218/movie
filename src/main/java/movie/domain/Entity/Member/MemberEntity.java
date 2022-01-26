package movie.domain.Entity.Member;

import lombok.*;
import movie.domain.Entity.BaseTimeEntity;
import movie.domain.Entity.Movie.ReplyEntity;
import movie.domain.Entity.Ticketing.TicketingEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
@NoArgsConstructor@AllArgsConstructor
@Setter@Getter@ToString@Builder
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="mno") // 회원번호
    private int mno;
    @Column(name="mid") // 회원 아이디
    private String mid;
    @Column(name="mpassword")  // 회원 비밀번호
    private String mpassword;
    @Column(name="mname")   //  이름
    private String mname;
    @Column(name="memail")  //  이메일
    private String memail;
    @Column(name="mphone")  // 핸드폰번호
    private String mphone;
    @Column(name="maddress")  // 주소
    private String maddress;
    @Column(name="msex")  // 성별
    private String msex;
    @Column(name="mage")  // 나이
    private String mage;

    @OneToMany(mappedBy = "memberEntityReply", cascade = CascadeType.ALL) // 댓글리스트
    private List<ReplyEntity> replyEntities = new ArrayList<>();
    @OneToMany(mappedBy = "memberEntityTicket" , cascade = CascadeType.ALL) // 티켓 리스트
    private List<TicketingEntity> ticketingEntities = new ArrayList<>();


}
