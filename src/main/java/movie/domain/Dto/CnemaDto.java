package movie.domain.Dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CnemaDto {

    private List<String> cnemaact = new ArrayList<>();

    public CnemaDto(int width,int height) {
        for(char i='A'; i<'A'+height; i++){
            for(int j=1; j<width+1; j++){
                cnemaact.add(i+","+j);
            }
        }
    }



}
