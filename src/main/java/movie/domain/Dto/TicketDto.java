package movie.domain.Dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {

    private int tno;
    private String date;
    private String seat;
    private String count;
    private String movietitle;
    private String cnemaname;
    private String price;

}
