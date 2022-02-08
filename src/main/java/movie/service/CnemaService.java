package movie.service;

import movie.domain.Entity.Cnema.CnemaEntity;
import movie.domain.Entity.Cnema.CnemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    //관조회
    public List<CnemaEntity> getCnemalist(){
        List<CnemaEntity> cnemaEntityList = cnemaRepository.findAll();
        return cnemaEntityList;
    }


}
