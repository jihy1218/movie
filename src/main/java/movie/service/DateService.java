package movie.service;

import movie.domain.Dto.DateDto;
import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Cnema.CnemaRepository;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Date.DateRepository;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Movie.MovieRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DateService {

    @Autowired
    DateRepository dateRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CnemaRepository cnemaRepository;

    //등록
    public boolean datewrite(String ddate,String dtime,
                             int cno, int mvno){
        CnemaEntity cnemaEntity = cnemaRepository.findById(cno).get();
        MovieEntity movieEntity = movieRepository.findById(mvno).get();

        int dno = dateRepository.save(DateEntity.builder()
                        .ddate(ddate)
                        .dtime(dtime)
                        .dseat(cnemaRepository.findById(cno).get().getCcount())
                        .cnemaEntityDate(cnemaEntity)
                        .movieEntityDate(movieEntity)
                .build()).getDno();

        cnemaEntity.getDateEntityList().add(dateRepository.findById(dno).get());
        movieEntity.getDateEntityList().add(dateRepository.findById(dno).get());

        return true;
    }

    //조회
    public List<DateEntity> getdatelist(){
        List<DateEntity> dateEntityList = dateRepository.findAll();
        return dateEntityList;
    }

    //dno로 조회
    public DateEntity getdateentity(int dno){
        DateEntity dateentity = dateRepository.findById(dno).get();
        return dateentity;
    }

    //전체삭제
    public boolean delete(int type , int no){
        if(type==1){
            Optional<CnemaEntity> entity = cnemaRepository.findById(no);
            if(entity!=null){
                cnemaRepository.delete(entity.get());
            }
        }else if(type==2){
            Optional<MovieEntity> entity = movieRepository.findById(no);
            if(entity!=null){
                movieRepository.delete(entity.get());
            }
        }else{
            Optional<DateEntity> entity = dateRepository.findById(no);
            if(entity!=null){
                dateRepository.delete(entity.get());
            }
        }
        return true;
    }

    // 영화번호로 상영일 찾기
    public String datelist(int mvno){
        List<String> dates = dateRepository.findDateByMvno(mvno);
        String date= "";
        for(int i=0; i<dates.size(); i++){
            if(i==dates.size()-1){
                date += dates.get(i);
            }else {
                date += dates.get(i) + ",";
            }
        }
        return  date;
    }

    // 날짜로 시간 찾기
    public JSONObject timelist(String day,int mvno){
        JSONArray jsonArray = new JSONArray();
       List<DateEntity> times = dateRepository.findTimeByDate(day,mvno);
       for(int i=0;i<times.size();i++) {
           JSONObject jsonObject = new JSONObject();
           jsonObject.put("dno",times.get(i).getDno());                 // date pk
           jsonObject.put("ddate",times.get(i).getDdate());      // 상영일
           jsonObject.put("dtime",times.get(i).getDtime());        // 상영시간 ex) 17:00~19:00
           jsonObject.put("dseat",times.get(i).getDseat());       // 잔여좌석
           jsonObject.put("cno",times.get(i).getCnemaEntityDate().getCno());
           jsonObject.put("fullseat",times.get(i).getCnemaEntityDate().getCcount());
           jsonObject.put("ctype",times.get(i).getCnemaEntityDate().getCtype());
           jsonArray.add(jsonObject);
       }
        JSONObject king = new JSONObject();
        king.put("movie",jsonArray);
        return king;

    }



}
