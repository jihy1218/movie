package movie.controller;

import movie.domain.Dto.MemberDto;
import movie.domain.Dto.MovieDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Dto.TicketDto;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Movie.ReplyEntity;
import movie.domain.Entity.Ticketing.TicketingEntity;
import movie.domain.Entity.Ticketing.TicketingRepository;
import movie.service.DateService;
import movie.service.MemberService;
import movie.service.MovieService;
import movie.service.TicketingService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        model.addAttribute("memberDto",memberDto);
        String dates=null;
        for(MovieinfoDto temp : movielist){
            dates =dateService.datelist(temp.getMvno());
        }
        model.addAttribute("datelist",dates);
        return "movie/ticketingdate";
    }

    @Autowired
    TicketingService ticketingService;

    @GetMapping("/ticketingseat/{dno}")
    public  String ticketingseat(@PathVariable("dno")int dno,Model model){
        List<String> seatlist = ticketingService.getseatlist(dno,0);
        DateEntity dateentity = dateService.getdateentity(dno);
        JSONObject movieinfo = movieService.getmovieinfoselect(dateentity.getMovieEntityDate().getMvid());

        model.addAttribute("movieinfo",movieinfo);
        model.addAttribute("dateinfo" ,dateentity);
        model.addAttribute("seatlist",seatlist);

        return "movie/ticketingseat";}

    @Autowired
    HttpServletRequest request;
    @Autowired
    MemberService memberService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private DateService dateService;
    //댓글 추가하기
    @GetMapping("/replyadd")
    public String replyadd(@RequestParam("mvid")String mvid , Model model ,
                           @RequestParam("tbody")int tbody){

        List<ReplyEntity> replyEntitiys = movieService.getreplylist(mvid , tbody );
        model.addAttribute("replyEntitiys",replyEntitiys );

        return "movie/replytable";
    }

   @GetMapping("/movieview/{mvid}")
    public String movieview(@PathVariable("mvid")String mvid, Model model){ // 영화번호에 해당하는 엔티티 뽑아와야뎀
        JSONObject jsonObject = movieService.getmovieinfoselect(mvid);

        // 댓글
       List<ReplyEntity> replyEntitiys = movieService.getreplylist(mvid , 0);
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
        JSONObject rankjson = movieService.getranking(movieinfoDto.getMvno());
        double star = movieService.getstar(mvid);
        model.addAttribute("star",star);
        model.addAttribute("rank" ,rankjson);
        model.addAttribute("movieview",movieinfoDto);
       model.addAttribute("replyEntitiys",replyEntitiys );
        return "movie/movieview";
    }


    // 댓글삭제
    @GetMapping("replydelete")
    @ResponseBody
    public int replydelete(@RequestParam("rno") int rno){
        boolean result = movieService.replydelete(rno);
        if(result){
            return 1;
        }else{
            return 2;
        }
    }



    //댓글 등록
    //2/11 여기에서  영화 번호 넣고 시작하면된다 js에도 영화 번호 넣어야함
    @GetMapping("/replywrite/")
    @ResponseBody
    public String replywrite( @RequestParam("mvno")int mvno,@RequestParam("rcontents") String rcontents){

        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        int mno = memberDto.getMno();

        if(memberDto==null){
            return "2";
        }else{
            movieService.replywrite1(mvno,rcontents,mno);
            return "1";
        }
    }




    // 영화 선택시
   @GetMapping("/movieselect")
   @ResponseBody
    public String movieselect(@RequestParam("mvno")int mvno){
        String dates=dateService.datelist(mvno);
        return dates;
    }


    @GetMapping("/ticketingcontroller")
    @ResponseBody
    public int ticketingcontroller(@RequestParam("tseat")String tseat,
                                      @RequestParam("tage")String tage,
                                      @RequestParam("tprice")String tprice,
                                      @RequestParam("dno")int dno,
                                      @RequestParam("count")int count){
         HttpSession session = request.getSession();
         MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
         int mno = memberDto.getMno();

        int result = ticketingService.ticketing(tseat,tage,tprice,dno,mno,count);
        if(result!=0){
            return result;
        }else{
            return 0;
        }

    }


    // 날짜 선택시
    @GetMapping("/dateselect")
    @ResponseBody
    public JSONObject dateselect(@RequestParam("day")String day,@RequestParam("mvno")int mvno){
        JSONObject jsonObject = dateService.timelist(day,mvno);
        return jsonObject;
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

    // 예약확인 페이지
    @GetMapping("/reservation/{tno}")
    public String reservation(@PathVariable("tno")int tno,Model model){
        TicketDto ticketing = ticketingService.getTicket(tno);
        model.addAttribute("ticket",ticketing);
       return "movie/reservation";
    }

    // 무비홈 페이지 매핑
    @GetMapping("/moviehome")
    public String movieHome(Model model){
        List<MovieinfoDto> movieinfoDtos = movieService.getmovieinfo();
        List<MovieinfoDto> movieinfoDtoList = new ArrayList<>();
        for(int i=0; i<movieinfoDtos.size(); i++){
            movieinfoDtoList.add(movieinfoDtos.get(i));
        }
        List<MovieinfoDto> top4list = movieService.gettop4();
        model.addAttribute("top4",top4list);
        model.addAttribute("movie",movieinfoDtoList);
       return "movie/moviehome";
    }
    // 준비중 페이지 매핑
    @GetMapping("/repair")
    public String repair(){
       return "repair";
    }

    // 문자 전송 매핑
    @GetMapping("/sms")
    @ResponseBody
    public String sendsms(@RequestParam("movieNm")String movieNm,@RequestParam("cinema")String cinema,@RequestParam("movieTime")String movieTime,@RequestParam("movieSeat")String movieSeat,@RequestParam("phoneNumber")String phoneNumber){
        movieService.sendSms(phoneNumber,movieNm,cinema,movieTime,movieSeat);
        return "1";
    }






}

