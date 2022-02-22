package movie.service;

import movie.domain.Dto.DateDto;
import movie.domain.Dto.MovieinfoDto;
import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Cnema.CnemaRepository;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Date.DateRepository;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Movie.MovieEntity;
import movie.domain.Entity.Movie.MovieRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Page<DateEntity> getDatelist(Pageable pageable , String keyword, String search){
        //페이지 번호
        int page = 0;
        if(pageable.getPageNumber() ==0) page = 0;
        else page = pageable.getPageNumber()-1;
        //페이지 속성
        pageable = PageRequest.of(page,10); //, Sort.by(Sort.Direction.DESC,"mno")
        String abc = "20028";

        if (  keyword !=null && keyword.equals("dno") ) return dateRepository.findBydno( search , pageable );
        if (  keyword !=null && keyword.equals("mvid") ) return dateRepository.findBymvno( movieRepository.findmvidbymvno(search) , pageable );
        if (  keyword !=null && keyword.equals("ddate") ) return dateRepository.findByddate( search , pageable );

        return dateRepository.findAll(pageable);
    }

    //조회
    public List<DateEntity> getdatelist(int tbody){
        List<DateEntity> dateEntityList = dateRepository.findAll();
        List<DateEntity>result = new ArrayList<>();
        int count = 3;
        int 남은갯수 = dateEntityList.size()-tbody;

        if(남은갯수<count){
            for(int i = tbody; i<tbody+(남은갯수); i++){
                result.add(dateEntityList.get(i));
            }
        }else{
            for( int i = tbody ; i<tbody+count ; i++ ){
                result.add(dateEntityList.get(i));
            }
        }

        return result;
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
