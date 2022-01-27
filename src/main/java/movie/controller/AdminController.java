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
    public String adminmain(){

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

        for(int i = 1; i<4; i++){
            for(int j = 1; j<4; j++){
                arrayList.add(i+","+j);
            }
        }
        model.addAttribute("list" , arrayList);
        return "admin/cnemaregister";
    }

    @PostMapping("/moviewritecontroller")
    @ResponseBody
    public String moviewritecontroller(@RequestParam("mvimg") MultipartFile file){
        String mvid =request.getParameter("mvid");
        try{
            UUID uuid = UUID.randomUUID();
            String filename = file.getOriginalFilename();
            String uuidfile = uuid.toString()+"_"+filename.replaceAll("_","-");
            if(!file.getOriginalFilename().equals("")){
                //String dir = "C:\\Users\\505\\Desktop\\movie\\src\\main\\resources\\static\\poster"; //민욱이 주소
                String dir = "C:\\Users\\505\\Desktop\\Spring\\movie\\src\\main\\resources\\static\\foster";
                String filepath = dir+"\\" + uuidfile;
                file.transferTo(new File((filepath)));
            }else{
                uuidfile=null;
            }
            MovieDto moviedto = MovieDto.builder()
                    .mvid(mvid)
                    .mvimg(uuidfile)
                    .build();
            movieService.moviewrite(moviedto);

        }catch(Exception e){

        }
        return "1";
    }

    @GetMapping("/movielist")
    public String movielist(Model model) {
        List<MovieinfoDto> movieDtos = movieService.getmovieinfo();
            model.addAttribute("movieinfo" , movieDtos);
        return "admin/movielist";
    }


}
