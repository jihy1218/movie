package movie.service;

import movie.domain.Dto.MovieDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Movie.MovieRepository;
import movie.domain.Entity.Movie.MoviefileEnity;
import movie.domain.Entity.Movie.MoviefileRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MoviefileRepository moviefileRepository;
    //영화 등록
    public boolean moviewrite(String mvid, List<MultipartFile> mvimg, List<MultipartFile> mvvideo){
        String dirPo = "C:\\Users\\505\\Desktop\\movie\\src\\main\\resources\\static\\poster";
        String dirVi = "C:\\Users\\505\\Desktop\\movie\\src\\main\\resources\\static\\video";
        MovieDto movieDto = MovieDto.builder()
                .mvid(mvid)
                .build();
        int mno =  movieRepository.save(movieDto.toEntity()).getMvno();
        MovieEntity movieEntity = movieRepository.findById(mno).get();
        System.out.println("abc :"+movieEntity.toString());
        String uuidfile = null;
        if(mvimg.size()!=0){
            for(MultipartFile img : mvimg){
                UUID uuid = UUID.randomUUID();
                uuidfile = uuid.toString()+"_"+img.getOriginalFilename().replaceAll("_","-");
                String filepath = dirPo+"\\"+uuidfile;
                try{
                    img.transferTo(new File(filepath));
                }catch(Exception e){
                    e.printStackTrace();
                }
                MoviefileEnity moviefileEnity = MoviefileEnity.builder()
                        .mvfile(uuidfile)
                        .mvtype(1)
                        .movieEntity(movieEntity)
                        .build();
                int mfileno =moviefileRepository.save(moviefileEnity).getMvfileno();
                MoviefileEnity moviefileEnity1= moviefileRepository.findById(mfileno).get();
                System.out.println("abc2 :"+movieEntity.toString());
                System.out.println("moviefileEnity1 : "+moviefileEnity1);
                movieEntity.getMoviefileEnities().add(moviefileEnity1);
                //movieEntity.getMoviefileEnities().add(moviefileRepository.findById(mfileno).get());
            }
        }
        return true;
    }

    //영화목록조회
    public List<MovieDto> getmovielist(){
        List<MovieEntity> movieEntities = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();
        for(MovieEntity movie : movieEntities){
            MovieDto movieDto = MovieDto.builder()
                    .mvid(movie.getMvid())
                    .mvimg(movie.getMvimg())
                    .build();
            movieDtos.add(movieDto);
        }
        return movieDtos;
    }

    //박스오피스 api가져오기
    public JSONArray getmovie(){
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
            return jsonArray;
        } catch (Exception e) {
        }
        return null;
    }



    //영화상세정보api
    public List<MovieinfoDto> getmovieinfo(){
        List<MovieDto> list = this.getmovielist();
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
        return movieDtos;
    }

    //선택영화상세정보api
    public JSONObject getmovieinfoselec(String mvid){
        JSONObject jsonObject0 = new JSONObject();
            String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=7e83198258b5dd58ff5ca336a95ff5e8&movieCd="+mvid;
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

                jsonObject0.put("movieNm",jsonObject3.get("movieNm"));
                jsonObject0.put("showTm",jsonObject3.get("showTm"));
                jsonObject0.put("openDt",jsonObject3.get("openDt"));
                jsonObject0.put("nations",nation.get("nationNm"));
                jsonObject0.put("genres",genres.get("genreNm"));
                jsonObject0.put("directors",directors.get("peopleNm"));
                jsonObject0.put("actors",actors);
                jsonObject0.put("companyNm",(String)companys.get("companyNm"));
                jsonObject0.put("watchGradeNm",audits.get("watchGradeNm"));

                return jsonObject0;
            } catch (Exception e) {
            }
        return null;
    }



}
