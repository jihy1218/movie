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

        try {
            URL url = new URL("https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchWeeklyBoxOfficeList.json?key=7e83198258b5dd58ff5ca336a95ff5e8&targetDt=20220123");
            BufferedReader bf = new BufferedReader( new InputStreamReader(  url.openStream() , "UTF-8") );

            String result = bf.readLine();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            JSONObject jsonObject2 = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray jsonArray = (JSONArray) jsonObject2.get("weeklyBoxOfficeList");
            System.out.println( jsonArray.toString() );

            for( int i = 0 ; i<jsonArray.size() ; i++ ) {
                JSONObject content = (JSONObject) jsonArray.get(i);
                System.out.println( content.get("movieNm") );
            }

            model.addAttribute("movielist",jsonArray);
        } catch (Exception e) {
        }
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
        System.out.println(mvid);
        System.out.println(file.getOriginalFilename());
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
        List<MovieDto> list = movieService.getmovielist();
        List<MovieinfoDto> movieDtos = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();

            for(MovieDto temp : list){
                String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=7e83198258b5dd58ff5ca336a95ff5e8&movieCd="+temp.getMvid();
                try {
                    URL url = new URL(urlpa);
                    BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String result = bf.readLine();
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                    JSONObject jsonObject2 = (JSONObject) jsonObject.get("movieInfoResult");
                    JSONObject jsonObject3 = (JSONObject) jsonObject2.get("movieInfo");
                    JSONArray nations = (JSONArray) jsonObject3.get("nations");
                    JSONObject nation = (JSONObject)nations.get(0);
                    JSONArray genresJS = (JSONArray)  jsonObject3.get("genres");
                    JSONObject genres = (JSONObject)genresJS.get(0);
                    JSONArray directorsJS = (JSONArray)  jsonObject3.get("directors");
                    JSONObject directors = (JSONObject)directorsJS.get(0);
                    JSONArray actorsJSON = (JSONArray) jsonObject3.get("actors");
                    JSONArray companysJS = (JSONArray) jsonObject3.get("companys");
                    JSONObject companys = (JSONObject)companysJS.get(0);

                    JSONArray auditsJS = (JSONArray) jsonObject3.get("audits");
                    JSONObject audits = (JSONObject)auditsJS.get(0);
                    String actors = "" ;
                    for(int i = 0; i<actorsJSON.size(); i++){
                        JSONObject abc = (JSONObject)actorsJSON.get(i);
                        actors += abc.get("peopleNm")+",";
                    }
                    System.out.println(actors);
                    MovieinfoDto movieDto = MovieinfoDto.builder()
                            .movieNm((String)jsonObject3.get("movieNm"))
                            .showTm(Integer.parseInt((String)jsonObject3.get("showTm")))
                            .openDt((String)jsonObject3.get("openDt"))
                            .nations((String) nation.get("nationNm"))
                            .genres((String)genres.get("genreNm"))
                            .directors((String)directors.get("peopleNm"))
                            .actors(actors)
                            .companyNm((String)companys.get("companyNm"))
                            .watchGradeNm((String)audits.get("watchGradeNm"))
                            .build();

                    movieDtos.add(movieDto);
                } catch (Exception e) {
                }
            }
        System.out.println(movieDtos.toString());
            model.addAttribute("movieinfo" , movieDtos);
        return "admin/movielist";
    }


}
