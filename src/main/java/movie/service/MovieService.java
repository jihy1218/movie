package movie.service;

import movie.domain.Dto.MovieDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.MemberRepository;
import movie.domain.Entity.Movie.*;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MoviefileRepository moviefileRepository;
    //영화 등록

    @Transactional
    public boolean moviewrite(String mvid, List<MultipartFile> mvimg, List<MultipartFile> mvvideo ,MultipartFile mvposter){
        String dirPo = "C:\\Users\\505\\Desktop\\Spring\\movie\\src\\main\\resources\\static\\poster";
        String dirVi = "C:\\Users\\505\\Desktop\\movie\\src\\main\\resources\\static\\video";
        MovieDto movieDto = MovieDto.builder()
                .mvid(mvid)
                .build();
        int mno =  movieRepository.save(movieDto.toEntity()).getMvno();
        MovieEntity movieEntity = movieRepository.findById(mno).get();
        String uuidfile = null;
        System.out.println("mvposter:"+mvposter.getOriginalFilename());
        if(!mvimg.get(0).getOriginalFilename().equals("")){
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
                        .mvtype(2)
                        .movieEntityFile(movieEntity)
                        .build();
                int mfileno =moviefileRepository.save(moviefileEnity).getMvfileno();
                movieEntity.getMoviefileEnities().add(moviefileRepository.findById(mfileno).get());
            }
        }
        if(!mvvideo.get(0).getOriginalFilename().equals("")){
            for(MultipartFile img : mvvideo){
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
                        .mvtype(3)
                        .movieEntityFile(movieEntity)
                        .build();
                int mfileno =moviefileRepository.save(moviefileEnity).getMvfileno();
                movieEntity.getMoviefileEnities().add(moviefileRepository.findById(mfileno).get());
            }
        }
        if(!mvposter.getOriginalFilename().equals("")){
            UUID uuid = UUID.randomUUID();
            uuidfile = uuid.toString()+"_"+mvposter.getOriginalFilename().replaceAll("_","-");
            String filepath = dirPo+"\\"+uuidfile;
            try{
                mvposter.transferTo(new File(filepath));
            }catch(Exception e){
                e.printStackTrace();
            }
            MoviefileEnity moviefileEnity = MoviefileEnity.builder()
                    .mvfile(uuidfile)

                    .mvtype(1)

                    .movieEntityFile(movieEntity)
                    .build();
            int mfileno =moviefileRepository.save(moviefileEnity).getMvfileno();
            movieEntity.getMoviefileEnities().add(moviefileRepository.findById(mfileno).get());
        }
        return true;
    }

    //영화목록조회
    public List<MovieDto> getmovielist(){
        List<MovieEntity> movieEntities = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();
        for(MovieEntity movie : movieEntities){
            MovieDto movieDto = MovieDto.builder()
                    .mno(movie.getMvno())
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
                for(int i = 0; i<3; i++){
                    JSONObject abc = (JSONObject)actorsJSON.get(i);
                    if(i==2) {
                        actors += abc.get("peopleNm");
                    }else {
                        actors += abc.get("peopleNm") + " ,";
                    }
                }
                System.out.println(actors);
               Optional<MovieEntity>  movieEntity = movieRepository.findById(temp.getMno());
               List<MoviefileEnity> moviefileEnity =  movieEntity.get().getMoviefileEnities();
               List<MovieinfoDto.MoviefileDto> moviefileDtoList = new ArrayList<>();
               String poster = null;
               List<String> movieimg = new ArrayList<>();
               List<String> movievideo  = new ArrayList<>();
               for(MoviefileEnity temp2 : moviefileEnity){
                   if(temp2.getMvtype()==1){
                       poster = temp2.getMvfile();
                   }else if(temp2.getMvtype()==2){
                       movieimg.add(temp2.getMvfile());
                   }else if(temp2.getMvtype()==3){
                       movievideo.add(temp2.getMvfile());
                   }
                       moviefileDtoList.add(temp2.toDto());
               }
                MovieinfoDto movieDto = MovieinfoDto.builder()
                        .mvno(temp.getMno())
                        .movieNm((String)jsonObject3.get("movieNm"))
                        .showTm(Integer.parseInt((String)jsonObject3.get("showTm")))
                        .openDt((String)jsonObject3.get("openDt"))
                        .nations((String) nation.get("nationNm"))
                        .genres((String)genres.get("genreNm"))
                        .directors((String)directors.get("peopleNm"))
                        .actors(actors)
                        .mvid(temp.getMvid())
                        .companyNm((String)companys.get("companyNm"))
                        .watchGradeNm((String)audits.get("watchGradeNm"))
                        .poster(poster)
                        .movieimg(movieimg)
                        .movievideo(movievideo)
                        .build();

                movieDtos.add(movieDto);
            } catch (Exception e) {
            }
        }
        return movieDtos;
    }

    //영화 상세정보api 끌어오기
    public JSONObject getmovieinfoselect(String mvid){
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
            for(int i = 0; i<3; i++){
                JSONObject abc = (JSONObject)actorsJSON.get(i);
                if(i==2) {
                    actors += abc.get("peopleNm");
                }else {
                    actors += abc.get("peopleNm") + " ,";
                }
            }
            Optional<MovieEntity>  movieEntity = movieRepository.findById(this.getMvno(mvid));
            List<MoviefileEnity> moviefileEnity =  movieEntity.get().getMoviefileEnities();
            List<MovieinfoDto.MoviefileDto> moviefileDtoList = new ArrayList<>();
            String poster = null;
            List<String> movieimg = new ArrayList<>();
            List<String> movievideo  = new ArrayList<>();
            for(MoviefileEnity temp2 : moviefileEnity){
                if(temp2.getMvtype()==1){
                    poster = temp2.getMvfile();
                }else if(temp2.getMvtype()==2){
                    movieimg.add(temp2.getMvfile());
                }else if(temp2.getMvtype()==3){
                    movievideo.add(temp2.getMvfile());
                }
                moviefileDtoList.add(temp2.toDto());
            }
            jsonObject0.put("movieNm",jsonObject3.get("movieNm"));
            jsonObject0.put("showTm",jsonObject3.get("showTm"));
            jsonObject0.put("openDt",jsonObject3.get("openDt"));
            jsonObject0.put("nations",nation.get("nationNm"));
            jsonObject0.put("genres",genres.get("genreNm"));
            jsonObject0.put("directors",directors.get("peopleNm"));
            jsonObject0.put("actors",actors);
            jsonObject0.put("companyNm",(String)companys.get("companyNm"));
            jsonObject0.put("watchGradeNm",audits.get("watchGradeNm"));
            jsonObject0.put("poster",poster);
            jsonObject0.put("movieimg",movieimg);
            jsonObject0.put("movievideo",movievideo);
            System.out.println(jsonObject0.get("showTm"));
            System.out.println(jsonObject0.get("poster"));
            return jsonObject0;
        } catch (Exception e) { }
        return null;
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
            for(int i = 0; i<3; i++){
                JSONObject abc = (JSONObject)actorsJSON.get(i);
                if(i==2) {
                    actors += abc.get("peopleNm");
                }else {
                    actors += abc.get("peopleNm") + " ,";
                }
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
    //mvid로 mvno찾기
    public int getMvno(String mvid){
        int mvno = movieRepository.findMvno(mvid);
        return mvno;
    }
   /*// 영화 번호 로 정보 불러오기
    public MovieDto getMovieDto(int mno){
        Optional<MovieEntity> movieEntity = movieRepository.findBymno(mno);
        return  MovieDto.builder()
                .mno(mno)
                .mvid(movieEntity.get().getMvid())
                .mvimg(movieEntity.get().getMvimg())
                .build();

    }*/

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReplyRepository replyRepository;
    //댓글 등록
    //2/11 여기에서  영화 번호 넣고 시작하면된다 js에도 영화 번호 넣어야함
    public boolean replywrite1(int mvno,String rcontents , int mno){
        Optional<MemberEntity>  memberEntity = memberRepository.findById(mno);
        Optional<MovieEntity> movieEntity = movieRepository.findById(mvno);
        ReplyEntity replyEntity =ReplyEntity.builder()
            .rcontents(rcontents)
            .memberEntityReply(memberEntity.get())
            .movieEntityReply(movieEntity.get())
            .build();

        //  리플에 저장 // 멤버리스트에저장 //  하여튼 거시기 저장
        replyRepository.save(replyEntity);
        return true;
    }



}// C end
