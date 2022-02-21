package movie.service;

import movie.domain.Dto.MemberDto;
import movie.domain.Dto.MovieDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Dto.NewsDto;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.MemberRepository;
import movie.domain.Entity.Member.ReviewEntity;
import movie.domain.Entity.Movie.*;
import movie.domain.Entity.Payment.PaymentEntity;
import movie.domain.Entity.Payment.PaymentRepository;
import movie.domain.Entity.Ticketing.TicketingEntity;
import movie.domain.Entity.Ticketing.TicketingRepository;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
        /*String dirPo = "C:\\Users\\505\\Desktop\\Spring\\moviedj\\src\\main\\resources\\static\\poster";*/
        String dirVi = "C:\\Users\\505\\Desktop\\movie\\src\\main\\resources\\static\\video";
        MovieDto movieDto = MovieDto.builder()
                .mvid(mvid)
                .build();
        int mno =  movieRepository.save(movieDto.toEntity()).getMvno();
        MovieEntity movieEntity = movieRepository.findById(mno).get();
        String uuidfile = null;
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
            //URL url = new URL("https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchWeeklyBoxOfficeList.json?key=7e83198258b5dd58ff5ca336a95ff5e8&targetDt=20220123"); 욱
            URL url = new URL("https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchWeeklyBoxOfficeList.json?key=102a22e0d8e3693e65e4384ea0843554&targetDt=20220123");
            BufferedReader bf = new BufferedReader( new InputStreamReader(  url.openStream() , "UTF-8") );

            String result = bf.readLine();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            JSONObject jsonObject2 = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray jsonArray = (JSONArray) jsonObject2.get("weeklyBoxOfficeList");

            for( int i = 0 ; i<jsonArray.size() ; i++ ) {
                JSONObject content = (JSONObject) jsonArray.get(i);
            }
            return jsonArray;
        } catch (Exception e) {
        }
        return null;
    }
    //영화 상세 정보 더보기 추가한거
    public List<MovieinfoDto> getmovieinfoadd(int tbody){

        List<MovieDto> list = this.getmovielist();
        List<MovieinfoDto> movieDtos = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();

        for(MovieDto temp : list){
            String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=102a22e0d8e3693e65e4384ea0843554&movieCd="+temp.getMvid();
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
                System.out.println("2132132131"+movieDto.toString());
                movieDtos.add(movieDto);
            } catch (Exception e) {
            }
        }

        List<MovieinfoDto> list1 = new ArrayList<>();

        int count = 3;
        int 남은갯수 = movieDtos.size()-tbody;

        if(남은갯수<count){
            for(int i = tbody; i<tbody+(남은갯수); i++){
                list1.add(movieDtos.get(i));
            }
        }else{
            for( int i = tbody ; i<tbody+count ; i++ ){
                list1.add(movieDtos.get(i));
            }
        }


        return list1;
    }



    //영화상세정보api
    public List<MovieinfoDto> getmovieinfo(){
        List<MovieDto> list = this.getmovielist();
        List<MovieinfoDto> movieDtos = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();

        for(MovieDto temp : list){
            //String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=7e83198258b5dd58ff5ca336a95ff5e8&movieCd="+temp.getMvid(); 욱
            String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=102a22e0d8e3693e65e4384ea0843554&movieCd="+temp.getMvid();
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
        // String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=7e83198258b5dd58ff5ca336a95ff5e8&movieCd="+mvid; 욱
        String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=102a22e0d8e3693e65e4384ea0843554&movieCd="+mvid;
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
            return jsonObject0;
        } catch (Exception e) { }
        return null;
    }
    //선택영화상세정보api
    public JSONObject getmovieinfoselec(String mvid){
        JSONObject jsonObject0 = new JSONObject();
       // String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=7e83198258b5dd58ff5ca336a95ff5e8&movieCd="+mvid; 욱
        String urlpa = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=102a22e0d8e3693e65e4384ea0843554&movieCd="+mvid;
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


    //댓글 등록

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private MemberRepository memberRepository;

    public boolean replywrite1(int mvno, String rcontents, int mno){

        Optional <MovieEntity> movieOptional= movieRepository.findById(mvno);
        Optional <MemberEntity> memberOptional = memberRepository.findById(mno);
        ReplyEntity replyEntity = ReplyEntity.builder()
                .rcontents(rcontents)
                .memberEntityReply(memberOptional.get())
                .movieEntityReply(movieOptional.get())
                .build();
        replyRepository.save(replyEntity);
        movieOptional.get().getReplyEntities().add(replyEntity);
        memberOptional.get().getReplyEntities().add(replyEntity);
        return false;

    }




    //해당 영화 댓글 출력
    public List<ReplyEntity>getreplylist(String mvid ,int tbody ){

        Optional<MovieEntity>movieOptional=movieRepository.findBymvid(mvid);

        int mvno = movieOptional.get().getMvno();
        List<ReplyEntity> rno = replyRepository.findRno(String.valueOf(mvno));

        List<ReplyEntity> resultlist = new ArrayList<>();
        int count = 3;

        //전체댓글개수 - 현재댓글 = 남은댓글개수
            //11    -  11    =     0
        int 남은댓글개수 =rno.size()-tbody;
        if(남은댓글개수<count){
            for( int i = tbody ; i<tbody+(남은댓글개수) ; i++ ){
                resultlist.add(  rno.get(i) );
            }
        }else{
            for( int i = tbody ; i<tbody+count ; i++ ){
                resultlist.add(  rno.get(i) );
            }
        }

        System.out.println();
        System.out.println("resultlist: "+resultlist.toString());
        return resultlist;
    }




    public boolean replydelete(int rno){

        Optional<ReplyEntity>entityOptional = replyRepository.findById(rno);
        if(entityOptional.get() !=null){
            replyRepository.delete(entityOptional.get());
            return true;
        }
        else{
            return false;
        }
    }





    @Autowired
    TicketingRepository ticketingRepository;
    // 성별 통계
    public JSONObject getsexpercent(int mvno){
        int mcount = 0;
        int wcount = 0;
        MovieEntity movieEntity = movieRepository.findById(mvno).get();
        List<DateEntity> datelist = movieEntity.getDateEntityList();
        for(DateEntity dateE : datelist){
            List<TicketingEntity> ticketinglist = dateE.getTicketingEntities();
            for(TicketingEntity ticketE : ticketinglist){
                if(ticketE.getMemberEntityTicket().getMsex().equals("남")){
                    mcount++;
                }else if (ticketE.getMemberEntityTicket().getMsex().equals("여")){
                    wcount++;
                }
            }
        }

        int total = mcount + wcount;
        double mpercent = ((double)mcount/(double)total*100);
        double wpercent = ((double)wcount/(double)total*100);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mpercent",Math.ceil(mpercent*100)/100.0);
        jsonObject.put("wpercent",Math.ceil(wpercent*100)/100.0);
        return jsonObject;
    }

    // 연령 통계
    public JSONObject getagepercent(int mvno){
        int teencount = 0;
        int twentiescount = 0;
        int thirtiescount = 0;
        int older = 0 ;
        MovieEntity movieEntity = movieRepository.findById(mvno).get();
        List<DateEntity> datelist = movieEntity.getDateEntityList();
        for(DateEntity dateE : datelist){
            List<TicketingEntity> ticketinglist = dateE.getTicketingEntities();
            for(TicketingEntity ticketE : ticketinglist){
                if(Integer.parseInt(ticketE.getMemberEntityTicket().getMage())>0&&
                        Integer.parseInt(ticketE.getMemberEntityTicket().getMage())<20){
                    teencount++;
                }else if (Integer.parseInt(ticketE.getMemberEntityTicket().getMage())>19&&
                        Integer.parseInt(ticketE.getMemberEntityTicket().getMage())<30){
                    twentiescount++;
                }else if (Integer.parseInt(ticketE.getMemberEntityTicket().getMage())>29&&
                        Integer.parseInt(ticketE.getMemberEntityTicket().getMage())<40){
                    thirtiescount++;
                }else {
                    older++;
                }
            }
        }

        int total = teencount + twentiescount + thirtiescount + older;
        double teenpercent = ((double)teencount/(double)total*100);
        double twentiespercent = ((double)twentiescount/(double)total*100);
        double thirtiespercent = ((double)thirtiescount/(double)total*100);
        double olderpercent = ((double)older/(double)total*100);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("teen",Math.ceil(teenpercent*100)/100.0);
        jsonObject.put("twenties",Math.ceil(twentiespercent*100)/100.0);
        jsonObject.put("thirties",Math.ceil(thirtiespercent*100)/100.0);
        jsonObject.put("older",Math.ceil(olderpercent*100)/100.0);

        return jsonObject;
    }

    //예매율,순위,누적관객
    public JSONObject getranking(int mvno){
        int rankcount = 0;
        int totalcount =0;
        int totalcountall =0;
        List<Integer> list = new ArrayList<>();

        MovieEntity movieEntity = movieRepository.findById(mvno).get();
        List<MovieEntity> movielist = movieRepository.findAll();
        List<DateEntity> datelist = movieEntity.getDateEntityList();
        for(DateEntity date : datelist){
            List<TicketingEntity> ticketlist = date.getTicketingEntities();
            for(TicketingEntity ticket :ticketlist){
                try{
                    JSONParser jsonParser = new JSONParser();
                    JSONObject ticketjson = (JSONObject) jsonParser.parse(ticket.getTage());
                    totalcount += Integer.parseInt(String.valueOf(ticketjson.get("youth")));
                    totalcount += Integer.parseInt(String.valueOf(ticketjson.get("adult")));
                }catch(Exception e){}
            }
        }
        List<TicketingEntity> ticketingEntity = ticketingRepository.findAll();
        for(TicketingEntity ticket : ticketingEntity){
            try{
                JSONParser jsonParser = new JSONParser();
                JSONObject ticketjson = (JSONObject) jsonParser.parse(ticket.getTage());
                totalcountall += Integer.parseInt(String.valueOf(ticketjson.get("youth")));
                totalcountall += Integer.parseInt(String.valueOf(ticketjson.get("adult")));
            }catch(Exception e){}
        }
        for(MovieEntity movie : movielist){
            List<DateEntity> date = movie.getDateEntityList();
            int moviecount = 0;
            for(DateEntity datetemp : date){
                List<TicketingEntity> ticket = datetemp.getTicketingEntities();
                for(TicketingEntity tickettemp : ticket){
                    try{
                        JSONParser jsonParser = new JSONParser();
                        JSONObject ticketjson = (JSONObject) jsonParser.parse(tickettemp.getTage());
                        moviecount += Integer.parseInt(String.valueOf(ticketjson.get("youth")));
                        moviecount += Integer.parseInt(String.valueOf(ticketjson.get("adult")));
                    }catch(Exception e){}
                }
            }
            list.add(moviecount);
        }
        Collections.sort(list, Collections.reverseOrder());

        for(int i = 0; i<list.size();i++){
            if(list.get(i)==totalcount){
                rankcount=i+1;
            }
        }
        double advancerate = ((double)totalcount/(double)totalcountall*100);
        DecimalFormat df = new DecimalFormat("###,###");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cumulative" , df.format(totalcount));
        jsonObject.put("rank" , rankcount+"위");
        jsonObject.put("advancerate" , Math.ceil(advancerate*100)/100.0+"%");

        return jsonObject;
    }

    //탑4 무비
    //예매율,순위,누적관객
    public  List<MovieinfoDto> gettop4(){
        Map<String,Integer> rankmap = new HashMap();
        List<MovieEntity> movielist = movieRepository.findAll();
        List<MovieinfoDto> movieinfoDtoList = new ArrayList<MovieinfoDto>();
        for(MovieEntity movie : movielist){
            List<DateEntity> date = movie.getDateEntityList();
            int moviecount = 0;
            for(DateEntity datetemp : date){
                List<TicketingEntity> ticket = datetemp.getTicketingEntities();
                for(TicketingEntity tickettemp : ticket){
                    try{
                        JSONParser jsonParser = new JSONParser();
                        JSONObject ticketjson = (JSONObject) jsonParser.parse(tickettemp.getTage());
                        moviecount += Integer.parseInt(String.valueOf(ticketjson.get("youth")));
                        moviecount += Integer.parseInt(String.valueOf(ticketjson.get("adult")));
                    }catch(Exception e){}
                }
            }
            rankmap.put(movie.getMvno()+"",moviecount);
        }
        List<String> listKeySet = new ArrayList<>(rankmap.keySet());
        Collections.sort(listKeySet, (value1, value2) -> (rankmap.get(value2).compareTo(rankmap.get(value1))));
        for(int i =0; i<4; i++){
            int num =Integer.parseInt(listKeySet.get(i));
            JSONObject jsonObject =this.getmovieinfoselect(movieRepository.getById(num).getMvid());
            MovieinfoDto movieinfoDto = MovieinfoDto.builder()
                    .mvno(num)
                    .mvid(movieRepository.getById(num).getMvid())
                    .movieNm((String)jsonObject.get("movieNm"))
                    .openDt((String)jsonObject.get("openDt"))
                    .showTm(Integer.valueOf((String) jsonObject.get("showTm")))
                    .nations((String)jsonObject.get("nations"))
                    .genres((String)jsonObject.get("genres"))
                    .directors((String)jsonObject.get("directors"))
                    .actors((String)jsonObject.get("actors"))
                    .companyNm((String)jsonObject.get("companyNm"))
                    .watchGradeNm((String)jsonObject.get("watchGradeNm"))
                    .poster((String)jsonObject.get("poster"))
                    .movieimg((List<String>)jsonObject.get("movieimg"))
                    .movievideo((List<String>)jsonObject.get("movievideo"))
                    .build();
            movieinfoDtoList.add(movieinfoDto);
        }

        return movieinfoDtoList;
    }
    @Autowired
    HttpServletRequest request;

    @Autowired
    PaymentRepository paymentRepository;

    public List<String> reviewtime(int type){
        HttpSession session = request.getSession();
        MemberDto memberDto =(MemberDto)session.getAttribute("logindto");
        List<TicketingEntity> ticketlist= memberRepository.findById(memberDto.getMno()).get().getTicketingEntities();
        List<PaymentEntity> paylist = paymentRepository.findpaylist(memberDto.getMid());
        List<String> result = new ArrayList<String>();
        List<String> result2 = new ArrayList<String>();
        Date now = new Date();
        System.out.println("ti :"+ticketlist);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd HH:mm");
        try{
            for(PaymentEntity ticketing : paylist){
                if(ticketing.getReviewact()==1&&ticketing.getPtype().equals("결제완료")){
                    String date =ticketing.getPtime().split("•")[0];
                    String time = ticketing.getPtime().split("•")[1].split("~")[1];
                    String date2 = date+" "+time;
                    Date date3 = formatter.parse(date2);
                    System.out.println("date3: " + date3);
                    if(date3.before(now)){
                        result.add(ticketing.getTno()+"");
                    }else{
                        result2.add(ticketing.getTno()+"");
                    }
                }
            }
        }catch (Exception e){}
        if(type==1){
            return result;
        }else{
            return result2;
        }

    }

    public double getstar(String mvid){
        int total = 0;
        MovieEntity movieEntity=movieRepository.findentitybymvid(mvid);
        for(ReviewEntity reviewlist : movieEntity.getReviewEntities()){
            total += reviewlist.getRegrade();
        }
        double gradle = (double) total / (double) (5*movieEntity.getReviewEntities().size()) * 100.0;
        return gradle;
    }

    public List<NewsDto> crawlingdaum(){

        // Jsoup를 이용해서 http://www.cgv.co.kr/movies/ 크롤링
        String url = "https://www.joongang.co.kr/culture/movie/"; //크롤링할 url지정
        Document doc = null;        //Document에는 페이지의 전체 소스가 저장된다

        try {
            doc = Jsoup.connect(url).get();
            //select를 이용하여 원하는 태그를 선택한다. select는 원하는 값을 가져오기 위한 중요한 기능이다.
            Elements element = doc.select("ul.story_list");
            List<NewsDto> newsDtos = new ArrayList<>();
            System.out.println("============================================================");
            System.out.println(element.select("p.date").get(0));
            for(int i=0; i<4; i++){
                NewsDto news = NewsDto.builder()
                        .headline(String.valueOf(element.select("h2.headline").get(i)))
                        .description(String.valueOf(element.select("p.description").get(i)))
                        .date(String.valueOf(element.select("p.date").get(i)))
                        .image(String.valueOf(element.select("div.card_image").get(i)))
                        .build();
                newsDtos.add(news);
            }
            return newsDtos;

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("============================================================");

        return null;
    // sms 전송
    public void sendSms(String phoneNumber,String movieNm, String cinema, String movieTime, String movieSeat) {

        String api_key = "NCSQOZOP6GXW1JQW";
        String api_secret = "3SIZV6N77WVJAKSIMXTVG9XTQPGVZLV2";
        Message coolsms = new Message(api_key, api_secret);

        StringBuilder builder = new StringBuilder();
        builder.append(movieNm+","+cinema+"\r\n"+movieTime+"\r\n"+movieSeat);
        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "01046203976");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "[영화 예매내역]\r\n" +builder.toString());
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
        } catch (CoolsmsException e) {
        }

    }




}// C end
