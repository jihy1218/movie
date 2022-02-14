package movie.controller;

import movie.domain.Dto.MemberDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Member.MemberEntity;
import movie.service.DateService;
import movie.service.MemberService;
import movie.service.MovieService;
import movie.service.TicketingService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @GetMapping("/ticketingdate")
    public String ticketing(Model model){
        List<MovieinfoDto> movielist = movieService.getmovieinfo();
        model.addAttribute("movielist",movielist);
        String dates=null;
        for(MovieinfoDto temp : movielist){
            dates =dateService.datelist(temp.getMvno());

        }
       /* System.out.println(dates+"나오냐요오");*/
        model.addAttribute("datelist",dates);

        return "movie/ticketingdate";
    }

    @Autowired
    TicketingService ticketingService;

    @GetMapping("/ticketingseat")
    public  String ticketingseat(){return "movie/ticketingseat";}

    @Autowired
    HttpServletRequest request;
    @Autowired
    MemberService memberService;
    @GetMapping("/ticketingseat0")
    public  String ticketingseat0(Model model){
//         HttpSession session = request.getSession();
//         MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
//         int mno = memberDto.getMno();
//        int mno = 2;
//        MemberDto memberDto = memberService.getMemberDto(mno);
        int dno = 13;
        List<String> seatlist = ticketingService.getseatlist(dno);
        DateEntity dateentity = dateService.getdateentity(dno);
        JSONObject movieinfo = movieService.getmovieinfoselect(dateentity.getMovieEntityDate().getMvid());

        //model.addAttribute("memberDto",memberDto);
        model.addAttribute("movieinfo",movieinfo);
        model.addAttribute("dateinfo" ,dateentity);
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
    /*   System.out.println(movieinfoDto.toString()+"영화상세정보");*/
        JSONObject rankjson = movieService.getranking(movieinfoDto.getMvno());
        System.out.println(rankjson.toString());
        model.addAttribute("rank" ,rankjson);
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


    @GetMapping("/ticketingcontroller")
    @ResponseBody
    public String ticketingcontroller(@RequestParam("tseat")String tseat,
                                      @RequestParam("tage")String tage,
                                      @RequestParam("tprice")String tprice,
                                      @RequestParam("dno")int dno,
                                      @RequestParam("count")int count){
//         HttpSession session = request.getSession();
//         MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
//         int mno = memberDto.getMno();
        int mno = 2;

        boolean result = ticketingService.ticketing(tseat,tage,tprice,dno,mno,count);
        if(result){
            return "1";
        }else{
            return "2";
        }

    }


    // 날짜 선택시
    @GetMapping("/dateselect")
    @ResponseBody
    public String dateselect(@RequestParam("day")String day){
        String times= dateService.timelist(day);
        return times;
    }

    //댓글 등록
    //2/11 여기에서  영화 번호 넣고 시작하면된다 js에도 영화 번호 넣어야함
    @GetMapping("/replywrite/")
    @ResponseBody
    public String replywrite( @RequestParam("mvno")int mvno,@RequestParam("rcontents") String rcontents){
    HttpSession session = request.getSession();
    MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
    int mno = memberDto.getMno();
        boolean result = movieService.replywrite1(mvno,rcontents,memberDto.getMno());
       if(result){
            return "1";
        }else{
           return "2";
       }
    }

    //성별통계
    @GetMapping("/getsexpercent")
    @ResponseBody
    public JSONObject getsexpercent(@RequestParam("mvno")int mvno){

       JSONObject jsonObject = movieService.getsexpercent(mvno);

       return jsonObject;
    }

    //연령통계
    @GetMapping("/getagepercent")
    @ResponseBody
    public JSONObject getagepercent(@RequestParam("mvno")int mvno){
       JSONObject jsonObject = movieService.getagepercent(mvno);
       return jsonObject;
    }



}

