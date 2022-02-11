package movie.service;

import movie.domain.Dto.DateDto;
import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Cnema.CnemaRepository;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Date.DateRepository;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Movie.MovieRepository;
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
    public String timelist(String day){
        List<String> times = dateRepository.findTimeByDate(day);
        String time = "";
        for(int i=0;i<times.size();i++){
            if(i==times.size()-1){
                time += times.get(i);
            }else {
                time += times.get(i)+",";
            }
        }
        System.out.println(time);
        return time;
    }



}
