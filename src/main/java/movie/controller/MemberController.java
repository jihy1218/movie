package movie.controller;

import movie.domain.Dto.MemberDto;
import movie.domain.Dto.MovieinfoDto;
import movie.service.MemberService;
import movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(movieinfoDtoList+"영화이름 다나와");
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
    //회원가입페이지 연결
    @GetMapping("/member/myinfo")
    public String myinfo() {

        return "member/myinfo";
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
        System.out.println(memberDto.toString());
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

  /*  //로그인
   @PostMapping("/member/logincontroller")
    @ResponseBody
    public String logincontroller(@RequestBody MemberDto memberDto){
        //폼 사용시에는 자동 주입 0
        // AJAX 사용시에는 자동주입X -> @RequestBody
       MemberDto loginDto = memberService.login(memberDto);
       if(loginDto !=null){
           HttpSession session = request.getSession();
           session.setAttribute("logindto",loginDto);
           System.out.print("Login success");
           return "1";

       }else{
           System.out.print("Login fail");
           return "2";
       }


   }*/

}//class end
