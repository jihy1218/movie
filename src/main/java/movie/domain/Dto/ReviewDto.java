package movie.domain.Dto;


import lombok.*;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Movie.MovieEntity;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    private String recontents;
    private int regrade;
    private String mname ;
    private String mid ;
    private double repercent;

}
