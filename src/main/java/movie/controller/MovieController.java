package movie.controller;

import movie.domain.Dto.MovieinfoDto;
import movie.service.MovieService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @GetMapping("/ticketingdate")
    public String ticketing(Model model){
        List<MovieinfoDto> movielist = movieService.getmovieinfo();
        model.addAttribute("movielist",movielist);
        return "movie/ticketingdate";
    }

    @GetMapping("/ticketingseat")
    public  String ticketingseat(){return "movie/ticketingseat";}

    @Autowired
    private MovieService movieService;

    // 지형 여기까지 여기에 잠들다.....
   /* @GetMapping("/movieview")
    public String movieview(Model model){ // 영화번호에 해당하는 엔티티 뽑아와야뎀
        JSONObject jsonObject = movieService.getmovieinfoselect();
        List<MovieinfoDto> movieinfoDtos = new ArrayList<>();
        MovieinfoDto movieinfoDto = MovieinfoDto.builder()
                .movieNm((String)jsonObject.get("movieNm"))
                .openDt((String)jsonObject.get("openDt"))
                .nations((String)jsonObject.get("nationNm"))
                .genres((String)jsonObject.get("genreNm"))
                .directors((String)jsonObject.get("peopleNm"))
                .actors((String)jsonObject.get("actors"))
                .watchGradeNm((String)jsonObject.get("watchGradeNm"))
                .build();
        movieinfoDtos.add(movieinfoDto);
        model.addAttribute("movie",movieinfoDtos);
        return "movie/movieview";
    }*/

}
