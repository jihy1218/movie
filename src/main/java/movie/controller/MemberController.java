package movie.controller;

import movie.domain.Dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    @GetMapping("/")
    public String main(){
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

/*    //회원가입 ~! //여기서부터
    @PostMapping("/member/signupcontroller")
    public String signupcontroller(MemberDto memberDto,
                                   @RequestParam("address1" ))*/

}
