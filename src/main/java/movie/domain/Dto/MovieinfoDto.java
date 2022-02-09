package movie.domain.Dto;


import lombok.*;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Movie.MoviefileEnity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieinfoDto {

    private int mvno;
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
    // 영화포스터
    private String poster;
    //영화 파일 리스트
    List<MoviefileDto> moviefileDtos = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter@Setter@Builder
    public static class MoviefileDto {

        private int mvfileno;
        private String mvfile;
        private int mvtype;


    }

}
