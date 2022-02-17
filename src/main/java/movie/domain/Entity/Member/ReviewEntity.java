package movie.domain.Entity.Member;


import lombok.*;
import movie.domain.Entity.Movie.MovieEntity;

import javax.persistence.*;

@Entity
@Table(name="review")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reno") // 리뷰번호
    private int reno;

    @Column(name="recontents") // 리뷰내용
    private String recontents;

    @Column(name="regrade") // 리뷰평점
    private int regrade;

    @ManyToOne
    @JoinColumn(name="mno") // 회원 번호
    private MemberEntity memberEntityreview;

    @ManyToOne
    @JoinColumn(name="mvno") // 회원 번호
    private MovieEntity movieEntityreview;


}
