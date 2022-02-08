package movie.domain.Dto;

import lombok.*;
import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Ticketing.TicketingEntity;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateDto {

    private int dno;
    private String ddate;
    private String dtime;
    private CnemaEntity cnemaEntityDate;
    private MovieEntity movieEntityDate;
    private List<TicketingEntity> ticketingEntities = new ArrayList<>();


    public DateEntity toEntity(){
        DateEntity entity =DateEntity.builder()
                .ddate(this.ddate)
                .dtime(this.dtime)
                .movieEntityDate(this.movieEntityDate)
                .cnemaEntityDate(this.cnemaEntityDate)
                .ticketingEntities(this.ticketingEntities)
                .build();
        return entity;
    }
}
