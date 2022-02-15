package movie.service;

import movie.domain.Dto.CnemaDto;
import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Cnema.CnemaRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CnemaService {

    @Autowired
    CnemaRepository cnemaRepository;
    //관등록
    public boolean cnemawrite(String cnemajson , String cnematype ,String cnemaname ,int ccount ){

        CnemaEntity cnema = CnemaEntity.builder()
                .cactive(cnemajson)
                .cname(cnemaname)
                .ctype(cnematype)
                .ccount(ccount)
                .build();
        cnemaRepository.save(cnema);
        return true;
    }
    // 관 수정
    @Transactional
    public  boolean cnemaupdate(int cno,String cnemajson, String cnematype, String cnemaname, int ccount){
        Optional<CnemaEntity> cnemaEntity = cnemaRepository.findById(cno);
        return true;
    }
    //관조회
    public List<CnemaEntity> getCnemalist(){
        List<CnemaEntity> cnemaEntityList = cnemaRepository.findAll();
        return cnemaEntityList;
    }
    // 관번호로 관찾기
    public CnemaEntity cnemalist(int cno){
        Optional<CnemaEntity> cnema = cnemaRepository.findById(cno);
        return CnemaEntity.builder()
                .cno(cno)
                .cname(cnema.get().getCname())
                .ccount(cnema.get().getCcount())
                .cactive(cnema.get().getCactive())
                .ctype(cnema.get().getCtype())
                .build();
    }

}
