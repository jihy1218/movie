package movie.domain.Entity.Movie;

import lombok.*;
import movie.domain.Dto.MovieinfoDto;

import javax.persistence.*;

@Entity
@Table(name="moviefile")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = "movieEntityFile")
@Builder
public class MoviefileEnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mvfileno")
    private int mvfileno;

    @Column(name="mvfile")
    private String mvfile;

    @Column(name="mvtype")
    private int mvtype;

    @ManyToOne
    @JoinColumn(name="mvno")
    private MovieEntity movieEntityFile;

    public MovieinfoDto.MoviefileDto toDto(){
        MovieinfoDto.MoviefileDto entity = MovieinfoDto.MoviefileDto.builder()
                .mvfileno(this.mvfileno)
                .mvfile(this.mvfile)
                .mvtype(this.mvtype)
                .build();
        return entity;
    }
}
