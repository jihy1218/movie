package movie.domain.Dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NewsDto {
    private String headline;
    private String description;
    private String date;
    private String image;

}
