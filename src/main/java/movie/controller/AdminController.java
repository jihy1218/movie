package movie.controller;

import movie.domain.Dto.*;
import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Ticketing.TicketingEntity;
import movie.domain.Entity.Ticketing.TicketingRepository;
import movie.domain.Entity.Payment.PaymentEntity;
import movie.service.CnemaService;
import movie.service.DateService;
import movie.service.MovieService;
import movie.service.TicketingService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    MovieService movieService;


    //관등록 더보기 페이지
    @GetMapping("/cnemaadd")

    public String cnemaadd(Model model,@RequestParam("tbody") int tbody){
        List<CnemaEntity>cnemaEntityList =cnemaService.getCnemalistadd(tbody);
        model.addAttribute("cnemalist",cnemaEntityList );
        return "admin/admincnemaadd";
    }

    @GetMapping("/movieadd")
    public String movieadd(Model model,@RequestParam("tbody")int tbody){

        List<MovieinfoDto>movieDtos=movieService.getmovieinfoadd(tbody);
        model.addAttribute("movieinfo",movieDtos);
        return "admin/adminmovieadd";

    }
    // 영화 스케쥴
    @GetMapping("/movieinfoadd")
    public String movieinfoadd(Model model,@RequestParam("tbody")int tbody){
        List<DateEntity> dateEntityList = dateService.getdatelist(tbody);
        model.addAttribute("datelist",dateEntityList);
        return "admin/movieinfoadd";

    }



    @GetMapping("/adminmain")
    public String adminmain(Model model){
        List<CnemaEntity> cnemaEntityList = cnemaService.getCnemalistadd(0);
        model.addAttribute("cnemalist" , cnemaEntityList);
        List<MovieinfoDto> movieDtos = movieService.getmovieinfoadd(0);
        model.addAttribute("movieinfo" , movieDtos);
        List<DateEntity> dateEntityList = dateService.getdatelist(0);
        model.addAttribute("datelist",dateEntityList);
        movieService.gettop4();
        return "admin/adminmain";
    }

    @GetMapping("/moviewrite")
    public String moviewrite(Model model){

        JSONArray movielist = movieService.getmovie();
        model.addAttribute("movielist",movielist);
        return "admin/movieregister";

    }


    // 관등록 페이지 이동
    @GetMapping("/cnemawrite")
    public String cnemawrite(Model model){
        CnemaDto cnemaDto = new CnemaDto(20,10);
        List<String> cnemalist = cnemaDto.getCnemaact();
        model.addAttribute("list" , cnemalist);
        return "admin/cnemaregister";
    }

    @Autowired
    CnemaService cnemaService;

    //관등록
    @GetMapping("/cnemawritecontroller")
    @ResponseBody
    public String cnemawritecontroller(@RequestParam("cnema")String cnema,
                                       @RequestParam("cnematype")String cnematype,
                                       @RequestParam("cnemaname")String cnemaname){
        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(cnema);
            JSONArray jsonArray = (JSONArray) jsonObject.get("cnemalocation");
            boolean result = cnemaService.cnemawrite(cnema,cnematype,cnemaname,200-jsonArray.size());
            if(result){
                return "1";
            }else return "2";

        }catch(Exception e){
        }
        return "2";
    }

    @PostMapping("/moviewritecontroller")
    @ResponseBody
    public String moviewritecontroller(@RequestParam("mvimg")List<MultipartFile> mvimg,
                                       @RequestParam("mvvideo")List<MultipartFile> mvvideo,
                                       @RequestParam("mvposter")MultipartFile mvposter){
        String mvid =request.getParameter("mvid");
        movieService.moviewrite(mvid,mvimg,mvvideo,mvposter);
        return "1";
    }

    //상영시간페이지
    @GetMapping("/moviedate")
    public String moviedate(Model model){
        List<CnemaEntity> cnemaEntityList = cnemaService.getCnemalist();
        model.addAttribute("cnemalist" , cnemaEntityList);
        List<MovieinfoDto> movieDtos = movieService.getmovieinfo();
        model.addAttribute("movieinfo" , movieDtos);
        return "admin/screenregister";
    }

    //등록시 영화정보 불러오기
    @ResponseBody
    @PostMapping("/moviewriteinfo")
    //@RequestMapping(value="/moviewriteinfo" ,method=RequestMethod.POST)
    public JSONObject moviewriteinfo(@RequestParam("mvid")String mvid) {

        JSONObject jsonObject = movieService.getmovieinfoselec(mvid);

        return jsonObject;
    }
    @GetMapping("/movielist")
    public String movielist(Model model) {
        List<MovieinfoDto> movieDtos = movieService.getmovieinfo();
            model.addAttribute("movieinfo" , movieDtos);
        return "admin/movielist";
    }

    @Autowired
    DateService dateService;

    @GetMapping("/screenregister")
    @ResponseBody
    public String screenregister(@RequestParam("ddate")String ddate,
                                 @RequestParam("dtime")String dtime,
                                 @RequestParam("cno")int cno,
                                 @RequestParam("mvno")int mvno,
                                 @RequestParam("endtime")String endtime){
        String time = dtime+"~"+endtime;
        boolean result = dateService.datewrite(ddate,time,cno,mvno);
        if(result) {
            return "1";
        }else {return "2";}
    }

    @GetMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("type")int type,@RequestParam("no")int no){
        boolean result = dateService.delete(type,no);
        if(result){
            return "1";
        }else{
            return "2";
        }


    }

    @Autowired
    TicketingService ticketingService;

    @GetMapping("/datelist")
    public String datelist(@PageableDefault Pageable pageable , Model model){

        String keyword =request.getParameter("keyword");
        String search = request.getParameter("search");

        HttpSession session = request.getSession();

        if(keyword!=null || search!=null){
            session.setAttribute("keyword",keyword);
            session.setAttribute("search",search);
        }else{
            keyword=(String)session.getAttribute("keyword");
            search=(String)session.getAttribute("search");
        }
        Page<DateEntity> dateEntityList = dateService.getDatelist(pageable,keyword,search);

        model.addAttribute("datelist" ,dateEntityList);

        return "admin/Datelist";
    }

    @GetMapping("/memberadmin")
    public String memberadmin(@PageableDefault Pageable pageable , Model model){

        String keyword =request.getParameter("keyword");
        String search = request.getParameter("search");

        HttpSession session = request.getSession();

        if(keyword!=null || search!=null){
            session.setAttribute("keyword",keyword);
            session.setAttribute("search",search);
        }else{
            keyword=(String)session.getAttribute("keyword");
            search=(String)session.getAttribute("search");
        }

        Page<MemberEntity> memberlist = ticketingService.getmemberlist(pageable ,keyword ,search);

        model.addAttribute("memberlist",memberlist);

        return "admin/memberadmin";
    }

    @GetMapping("/memberticketing")
    public String memberticketing(@PageableDefault Pageable pageable , Model model){

        String keyword =request.getParameter("keyword");
        String search = request.getParameter("search");

        HttpSession session = request.getSession();

        if(keyword!=null || search!=null){
            session.setAttribute("keyword",keyword);
            session.setAttribute("search",search);
        }else{
            keyword=(String)session.getAttribute("keyword");
            search=(String)session.getAttribute("search");
        }

        Page<MemberEntity> memberlist = ticketingService.getmemberlist(pageable ,keyword ,search);

        model.addAttribute("memberlist",memberlist);

        return "admin/memberticketing";
    }
    @GetMapping("/ticketinglist/{mno}")
    public String ticketinglist(@PathVariable("mno") int mno, Model model){

        List<TicketDto> ticketing = ticketingService.getticketlist(mno);
        model.addAttribute("list",ticketing);
        return  "admin/memberticketing";
    }


    @GetMapping("/ticketingupdate/{tno}")
    public String ticketingupdate(@PathVariable("tno")int tno,Model model){
        int dno = ticketingService.finddno(tno);
        List<String> seatlist = ticketingService.getseatlist(dno,tno);
        DateEntity dateentity = dateService.getdateentity(dno);
        JSONObject movieinfo = movieService.getmovieinfoselect(dateentity.getMovieEntityDate().getMvid());
        int adult = ticketingService.getseatcount(tno,1);
        int youth = ticketingService.getseatcount(tno,2);

        model.addAttribute("adult",adult);
        model.addAttribute("youth",youth);
        model.addAttribute("movieinfo",movieinfo);
        model.addAttribute("dateinfo" ,dateentity);
        model.addAttribute("seatlist",seatlist);
        return "admin/ticketingupdate";
    }

    //어드민 예약취소
    @GetMapping("/ticketcancel")
    @ResponseBody
    public String ticketcancel(@RequestParam("tno")int tno){
        boolean result = ticketingService.ticketcancel(tno);
        if(result){
            return  "1";
        }
        return "2";
    }

    // 환불관리 페이지 이동
    @GetMapping("/paymentmanagement")
    public String paymentmanagement(@PageableDefault Pageable pageable,Model model){

        String keyword=request.getParameter("keyword");
        String search=request.getParameter("search");

        HttpSession session = request.getSession();

        Page<PaymentEntity> paymentEntities = ticketingService.paymentlist(pageable,keyword,search);
        model.addAttribute("payment",paymentEntities);
        return "admin/paymentmanagement";
    }

    // 환불관리 페이지 상태업데이트
    @GetMapping("/typeupdate")
    @ResponseBody
    public String typeupdate(@RequestParam("pno")int pno,@RequestParam("ptype")String ptype){
        boolean result = ticketingService.typeupdate(pno,ptype);
        if(result){
            return "1";
        }else{
            return "2";
        }
    }

    // 매출 페이지 이동
    @GetMapping("/sales")
    public String sales(){
        return "admin/sales";
    }
    // 매출페이지 차트 데이터 넣기
    @GetMapping("/salesdata")
    @ResponseBody
    public List<String> salesdata(@RequestParam("year")String year){
        List<String> ass = ticketingService.monthSales(year);
        return ass;
    }

}// class end
