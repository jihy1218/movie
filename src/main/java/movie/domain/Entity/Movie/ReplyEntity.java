package movie.domain.Entity.Movie;

import lombok.*;
import movie.domain.Entity.BaseTimeEntity;
import movie.domain.Entity.Member.MemberEntity;

import javax.persistence.*;

@Entity
@Table(name="reply")
@NoArgsConstructor@AllArgsConstructor
@Setter@Getter@ToString@Builder
public class ReplyEntity   extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rno")  // 댓글 번호
    private int rno;
    @Column(name="rcontents") // 댓글 내용
    private String rcontents;
    @Column(name="rgrade") // 평점 0~5 선택
    private int rgrade;
    @ManyToOne
    @JoinColumn(name="mno") // 회원 번호
    private MemberEntity memberEntityReply;
    @ManyToOne
    @JoinColumn(name="mvno")  // 영화 번호
    private MovieEntity movieEntityReply;

}
