package movie.controller;

import movie.domain.Dto.MovieinfoDto;
import movie.service.DateService;
import movie.service.MovieService;
import movie.service.TicketingService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    TicketingService ticketingService;

    @GetMapping("/ticketingseat")
    public  String ticketingseat(){return "movie/ticketingseat";}


    @GetMapping("/ticketingseat0")
    public  String ticketingseat0(Model model){

        List<String> seatlist = ticketingService.getseatlist();
        model.addAttribute("seatlist",seatlist);
        return "movie/ticketingseat";
    }

    @Autowired
    private MovieService movieService;

    @Autowired
    private DateService dateService;

   @GetMapping("/movieview/{mvid}")
    public String movieview(@PathVariable("mvid")String mvid, Model model){ // 영화번호에 해당하는 엔티티 뽑아와야뎀
        JSONObject jsonObject = movieService.getmovieinfoselect(mvid);
        MovieinfoDto movieinfoDto = MovieinfoDto.builder()
                .mvno(movieService.getMvno(mvid))
                .mvid(mvid)
                .movieNm((String)jsonObject.get("movieNm"))
                .openDt((String)jsonObject.get("openDt"))
                .showTm(Integer.valueOf((String) jsonObject.get("showTm")))
                .nations((String)jsonObject.get("nations"))
                .genres((String)jsonObject.get("genres"))
                .directors((String)jsonObject.get("directors"))
                .actors((String)jsonObject.get("actors"))
                .companyNm((String)jsonObject.get("companyNm"))
                .watchGradeNm((String)jsonObject.get("watchGradeNm"))
                .poster((String)jsonObject.get("poster"))
                .movieimg((List<String>)jsonObject.get("movieimg"))
                .movievideo((List<String>)jsonObject.get("movievideo"))
                .build();
       System.out.println(movieinfoDto.toString()+"영화상세정보");
        model.addAttribute("movieview",movieinfoDto);
        return "movie/movieview";
    }
    // 영화 선택시
   @GetMapping("/movieselect")
   @ResponseBody
    public String movieselect(@RequestParam("mvno")int mvno){
        String dates=dateService.datelist(mvno);
        System.out.println(dates+"해당날짜");
        return dates;
    }

    // 날짜 선택시
    @GetMapping("/dateselect")
    @ResponseBody
    public String dateselect(@RequestParam("day")String day){
        String times= dateService.timelist(day);
        return times;
    }
}
