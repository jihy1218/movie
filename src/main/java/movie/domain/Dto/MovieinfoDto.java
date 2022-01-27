package movie.domain.Dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieinfoDto {

    // mvid
    private String mvid;
    //제목
    private String movieNm;
    //상영시간
    private int showTm ;
    //개봉일
    private String openDt;
    //국가
    private String nations;
    //장르
    private String genres;
    //감독
    private String directors;
    //배우
    private String actors;
    //배급사
    private String companyNm;
    //등급
    private String watchGradeNm;

}
