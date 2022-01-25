package movie.domain.Dto;

import lombok.*;
import movie.domain.Entity.Movie.MovieEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {

    private int mno;
    private String mvid;
    private String mvimg;

    public MovieEntity toEntity(){
        MovieEntity entity = MovieEntity.builder()
                .mvid(mvid)
                .mvimg(mvimg)
                .build();
        return entity;
    }

}
