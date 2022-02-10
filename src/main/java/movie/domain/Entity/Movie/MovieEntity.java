package movie.domain.Entity.Movie;

import lombok.*;
import movie.domain.Entity.BaseTimeEntity;
import movie.domain.Entity.Date.DateEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="movie")
@NoArgsConstructor@AllArgsConstructor
@Setter@Getter@ToString(exclude ={"replyEntities","dateEntityList","moviefileEnities"})@Builder
public class MovieEntity  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mvno")   //  영화 번호
    private int mvno;
    @Column(name = "mvid")  // 영화 아이디
    private String mvid;
    @Column(name="mvimg")  // 영화 포스터
    private String mvimg;

    @OneToMany(mappedBy = "movieEntityReply",cascade = CascadeType.ALL) // 댓글 리스트
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "movieEntityDate", cascade = CascadeType.ALL)  // 영화 날짜 시간 리스트
    private List<DateEntity> dateEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "movieEntityFile", cascade = CascadeType.ALL)  // 영화 파일 리스트
    private List<MoviefileEnity> moviefileEnities = new ArrayList<>();

}
