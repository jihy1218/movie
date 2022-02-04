package movie.controller;

import movie.domain.Dto.MovieDto;
import movie.domain.Dto.MovieinfoDto;
import movie.service.MovieService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    MovieService movieService;

    @GetMapping("/adminmain")
    public String adminmain(Model model){
        List<MovieinfoDto> movieDtos = movieService.getmovieinfo();
        model.addAttribute("movieinfo" , movieDtos);
        return "admin/adminmain";
    }

    @GetMapping("/moviewrite")
    public String moviewrite(Model model){

        JSONArray movielist = movieService.getmovie();
        model.addAttribute("movielist",movielist);
        return "admin/movieregister";

    }

    @GetMapping("/cnemawrite")
    public String cnemawrite(Model model){

        ArrayList<String> arrayList = new ArrayList<String>();
        for(char i = 'A'; i<'A'+11; i++){
            for(int j = 1; j<21; j++){
                arrayList.add(i+","+j);
            }
        }
        model.addAttribute("list" , arrayList);
        return "admin/cnemaregister";
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
    public String moviedate(){
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


}
