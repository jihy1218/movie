package movie.service;

import movie.domain.Dto.MovieDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Movie.MovieRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;
    //영화 등록
    public boolean moviewrite(MovieDto movieDto){
        movieRepository.save(movieDto.toEntity());
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

}
