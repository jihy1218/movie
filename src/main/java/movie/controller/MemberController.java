package movie.controller;

import movie.domain.Dto.MemberDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Dto.NewsDto;
import movie.domain.Dto.TicketDto;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Movie.ReplyEntity;
import movie.domain.Entity.Payment.PaymentEntity;
import movie.domain.Entity.Ticketing.TicketingEntity;
import movie.service.MemberService;
import movie.service.MovieService;
import movie.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MovieService movieService;

    @GetMapping("/")
    public String main(Model model){
        List<MovieinfoDto> movieinfoDtos = movieService.getmovieinfo();
        List<MovieinfoDto> movieinfoDtoList = new ArrayList<>();
        for(int i=0; i<movieinfoDtos.size(); i++){
            movieinfoDtoList.add(movieinfoDtos.get(i));
        }
        List<NewsDto> news = movieService.crawlingdaum();
        model.addAttribute("news",news);
        model.addAttribute("movie",movieinfoDtoList);
        return "main";

    }

    //로그인페이지 연결
    @GetMapping("/member/login")
    public String login() {

        return "member/login";
    }
    //회원가입페이지 연결
    @GetMapping("/member/signup")
    public String signup() {

        return "member/signup";
    }



    @Autowired
    TicketingService ticketingService;
    //회원정보 페이지 연결
    @GetMapping("/member/myinfo")
    public String myinfo(@PageableDefault Pageable pageable, Model model) {
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        MemberDto member = memberService.getMemberDto(memberDto.getMno());
        int mno = memberDto.getMno();
        List<TicketDto> ticketDto = ticketingService.getticketlist(mno);
        List<PaymentEntity> paymentEntities = memberService.memberpaymentadd(member.getMid(),0);
        model.addAttribute("payment",paymentEntities);
        model.addAttribute("info", member);
        return "member/myinfo";
    }

    @GetMapping("/member/infoadd")
    public String infoadd(@RequestParam("mid")String mid , Model model ,
                           @RequestParam("tbody")int tbody){
        List<PaymentEntity> paymentEntities = memberService.memberpaymentadd(mid,tbody);
        model.addAttribute("payment",paymentEntities );

        return "member/myinfotable";
    }

    //회원가입
    @PostMapping("/member/signupcontroller")
    public String signupcontroller(MemberDto memberDto,
                                   @RequestParam("address1")String address1,
                                   @RequestParam("address2")String address2,
                                   @RequestParam("address3")String address3,
                                   @RequestParam("address4")String address4
                                   ){
        memberDto.setMaddress(address1+"/"+address2+"/"+address3+"/"+address4);
        memberService.membersignup(memberDto);
        return "member/login";
    }

    //아이디 중복체크
    @GetMapping("/member/idcheck")
    @ResponseBody
    public String idcheck(@RequestParam("mid") String mid){
        boolean result = memberService.idcheck(mid);
        if(result){
            return "1";
        }else{
            return "2";
        }
    }

    //이메일 중복체크
    @GetMapping("/member/emailcheck")
    @ResponseBody
    public String emailcheck(@RequestParam("memail")String mid){
        boolean result = memberService.emailcheck(mid);
        if(result){
            return "1";

        }else{
            return "2";
        }
    }


   // 아이디 찾기
    @GetMapping("/member/findid")
    @ResponseBody
    public String findid(@RequestParam("name")String name,@RequestParam("email")String email){
        String result= memberService.findid(name, email);
        return result;
    }
    // 비밀번호 찾기 메일전송
    @GetMapping("/member/findpassword")
    @ResponseBody
    public String findpasswrod(@RequestParam("id")String id, @RequestParam("email")String email){
        boolean result = memberService.findpassword(id,email);
        if(result){
            return "1";
        }else{
            return "2";
        }
    }
    // 회원정보 수정
    //비밀번호 수정
    @GetMapping("/member/passwordchange")
    @ResponseBody
    public String passwordchange(@RequestParam("mno")int mno,@RequestParam("password")String password,@RequestParam("type") int type){

        // type 1은 비밀번호 수정
        if(type==1) {
            boolean result = memberService.infoupdate(mno, password, type);
            if (result)
            {return "1";}
        }return "2";
    }
    // 핸드폰 번호 수정
    @GetMapping("/member/phonechange")
    @ResponseBody
    public String phonechange(@RequestParam("mno")int mno,@RequestParam("phone")String phone,@RequestParam("type")int type){
        if(type==2) {
            boolean result = memberService.infoupdate(mno, phone, type);
            if (result) {
                return "1";
            }
        }return "2";
    }
    // 주소 수정
    @GetMapping("/member/addresschange")
    @ResponseBody
    public String addresschange(@RequestParam("mno")int mno,@RequestParam("address1")String address1,@RequestParam("address2")String address2,@RequestParam("address3")String address3,@RequestParam("address4")String address4,@RequestParam("type")int type){
        String address=address1+"/"+address2+"/"+address3+"/"+address4;
        if(type==3){
            boolean result= memberService.infoupdate(mno,address,type);
            if(result){
                return "1";
            }
        }return  "2";
    }
    //회원 탈퇴
    @GetMapping("/member/delete")
    @ResponseBody
    public int mdelete( @RequestParam("passwordconfirm") String passwordconfirm){
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        boolean result = memberService.delete(memberDto.getMno(),passwordconfirm);
        if(result){ return 1;}
       else{ return 2;}

    }



    @GetMapping("/member/reviewwrite")
    @ResponseBody
    public String reviewwrite(@RequestParam("tno")int tno,
                              @RequestParam("grade")int grade,
                              @RequestParam("reviewcontents")String reviewcontents){
        boolean result = memberService.reviewwrite(tno,grade,reviewcontents);

        if(result){
            return "1";
        }else{
            return "2";
        }
    }
    @GetMapping("/member/reviewtime")
    @ResponseBody
    public List<String> reviewtime(){
        List<String> list = movieService.reviewtime(1);
        return list;
    }

    @GetMapping("/member/reviewtime2")
    @ResponseBody
    public List<String> reviewtime2(){
        List<String> list = movieService.reviewtime(2);
        return list;
    }

    //맴버환불요청
    @GetMapping("/member/refund")
    @ResponseBody
    public String refund(@RequestParam("tno")int tno){
        boolean result = memberService.ticketcancel(tno);
        if(result){
            return "1";
        }else{
            return "2";
        }
    }



}//class end
