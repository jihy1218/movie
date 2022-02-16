package movie.service;

import movie.domain.Dto.CnemaDto;
import movie.domain.Dto.TicketDto;
import movie.domain.Entity.Cnema.CnemaRepository;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Date.DateRepository;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.MemberRepository;
import movie.domain.Entity.Movie.MovieRepository;
import movie.domain.Entity.Payment.PaymentEntity;
import movie.domain.Entity.Payment.PaymentRepository;
import movie.domain.Entity.Ticketing.TicketingEntity;
import movie.domain.Entity.Ticketing.TicketingRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketingService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CnemaRepository cnemaRepository;

    @Autowired
    DateRepository dateRepository;

    @Autowired
    TicketingRepository ticketingRepository;

    public List<String> getseatlist(int dno ,int tno){

        //date 가져오기
        DateEntity dateentity = dateRepository.findById(dno).get();
        String cnemaact = dateentity.getCnemaEntityDate().getCactive();
        //좌석 가져오기
        CnemaDto cnemaDto = new CnemaDto(20,10);
        List<String> cnemaactlist = cnemaDto.getCnemaact();
        //예약정보 가져오기
        List<TicketingEntity> ticketing = dateentity.getTicketingEntities();
        ArrayList<String> ticketlist = new ArrayList<>();

        //json으로 바꾸기
        JSONParser jsonParser = new JSONParser();
        try{
            //좌석변경용
            List<String> updateticketlist = new ArrayList<>();
            if(tno!=0){
                TicketingEntity updateticket =ticketingRepository.findById(tno).get();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(updateticket.getTseat());
                JSONArray jsonArray = (JSONArray)jsonObject.get("tseat");
                for(int i =0; i<jsonArray.size(); i++){
                    JSONObject jsonObject1 =(JSONObject) jsonArray.get(i);
                    updateticketlist.add((String)jsonObject1.get("seat"));
                }
            }
            System.out.println("updateticketlist"+ updateticketlist.toString());

            //좌표 제이슨 -> 제이슨어레이
            JSONObject jsonObject = (JSONObject) jsonParser.parse(cnemaact);
            JSONArray jsonArray = (JSONArray) jsonObject.get("cnemalocation") ;
            ArrayList<String> exceptlist = new ArrayList<>();
            //좌표 제이슨 -> 리스트
            for(int i = 0; i<jsonArray.size(); i++){
                JSONObject exceptjson = (JSONObject)jsonArray.get(i);
                String   exceptString= (String) exceptjson.get("location");
                exceptlist.add(exceptString);
            }
            //예약좌석 제이슨 -> 리스트
            for(TicketingEntity temp : ticketing){
                JSONObject jsonObject1 = (JSONObject)jsonParser.parse(temp.getTseat());
                JSONArray jsonArray1 = (JSONArray) jsonObject1.get("tseat") ;
                for(int i = 0; i<jsonArray1.size(); i++){
                    JSONObject json = (JSONObject)jsonArray1.get(i);
                    String seatString= (String) json.get("seat");
                    ticketlist.add(seatString);
                }
            }
            //예약좌석 제이슨 -> 리스트


            ArrayList<Integer> list = new ArrayList<>();
            for(int i = 0; i< cnemaactlist.size();i++){
                for(String temp2 : exceptlist){
                    if(cnemaactlist.get(i).equals(temp2)){
                        list.add(i);
                    }
                }
            }
            // list = [0,9,19]
            for(Integer temp : list){
                int a = temp;
                int alength = a%20;
                if(alength!=19){
                    int abc =20-alength;
                    for(int i =temp+1; i<temp+abc; i++){
                        String value = cnemaactlist.get(i);
                        int ch = Integer.parseInt(value.split(",")[1])-1;
                        String fr = value.split(",")[0];
                        cnemaactlist.set(i,fr+","+ch);
                    }
                }
                cnemaactlist.set(temp,"X");

            }
            for(int i =0; i<cnemaactlist.size(); i++){

                for(String temp2 : ticketlist){
                    if(tno!=0){
                        for(String update : updateticketlist){
                            if(cnemaactlist.get(i).equals(update)){
                                cnemaactlist.set(i,cnemaactlist.get(i)+"#");
                            }
                        }
                    }
                    if(cnemaactlist.get(i).equals(temp2)){
                        cnemaactlist.set(i,cnemaactlist.get(i)+"@");
                    }
                }
            }

        }catch (Exception e){

        }
        return cnemaactlist;
    }
    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public boolean ticketing(String tseat,String tage,String tprice,
                             int dno,int mno,int count){
        DateEntity dateentity = dateRepository.findById(dno).get();
        MemberEntity memberEntity = memberRepository.findById(mno).get();

        dateentity.setDseat(dateentity.getDseat()-count);

        TicketingEntity ticketing = TicketingEntity.builder()
                .tseat(tseat)
                .tage(tage)
                .tprice(tprice)
                .memberEntityTicket(memberEntity)
                .dateEntityTicket(dateentity)
                .build();

        int tno=ticketingRepository.save(ticketing).getTno();

        dateentity.getTicketingEntities().add(ticketingRepository.findById(tno).get());
        memberEntity.getTicketingEntities().add(ticketingRepository.findById(tno).get());

        return true;
    }

    public Page<MemberEntity> getmemberlist(Pageable pageable , String keyword,String search){

        //페이지 번호
        int page = 0;
        if(pageable.getPageNumber() ==0) page = 0;
        else page = pageable.getPageNumber()-1;
        //페이지 속성
        pageable = PageRequest.of(page,10); //, Sort.by(Sort.Direction.DESC,"mno")

        if (  keyword !=null && keyword.equals("mno") ) return memberRepository.findAllmno( search , pageable );
        if (  keyword !=null && keyword.equals("mid") ) return memberRepository.findAllmid( search , pageable );
        if (  keyword !=null && keyword.equals("mname") ) return memberRepository.findAllmname( search , pageable );

        return memberRepository.findAll(pageable);
    }

    public Page<MemberEntity> getticketlist(Pageable pageable , String keyword,String search){

        //페이지 번호
        int page = 0;
        if(pageable.getPageNumber() ==0) page = 0;
        else page = pageable.getPageNumber()-1;
        //페이지 속성
        pageable = PageRequest.of(page,10); //, Sort.by(Sort.Direction.DESC,"mno")

        if (  keyword !=null && keyword.equals("mno") ) return memberRepository.findAllmno( search , pageable );
        if (  keyword !=null && keyword.equals("mid") ) return memberRepository.findAllmid( search , pageable );
        if (  keyword !=null && keyword.equals("mname") ) return memberRepository.findAllmname( search , pageable );

        return memberRepository.findAll(pageable);
    }
    JSONParser jsonParser = new JSONParser();
    @Autowired
    MovieService movieService;
    public List<TicketDto> getticketlist(int mno){
        try{
        List<TicketingEntity> list = ticketingRepository.findticketlist(mno);
        List<TicketDto> ticketdto = new ArrayList<>();
        for(TicketingEntity ticketing :list){
            TicketDto dto  = new TicketDto();
            dto.setTno(ticketing.getTno());
            dto.setDate(
                    ticketing.getDateEntityTicket().getDdate()+" "+ticketing.getDateEntityTicket().getDtime()
            );
            JSONObject jsonObject2 = (JSONObject)jsonParser.parse(ticketing.getTseat());
            JSONArray jsonArray = (JSONArray) jsonObject2.get("tseat");
            String seat = "";
            for( int i = 0; i<jsonArray.size(); i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                seat = seat + " " +jsonObject.get("seat")+"석";
            }
            dto.setSeat(seat);
            JSONObject jsonObject = (JSONObject)jsonParser.parse(ticketing.getTage());
            dto.setCount("어른 :"+String.valueOf(jsonObject.get("adult"))+
                    " 청소년 :"+String.valueOf(jsonObject.get("youth")));
            JSONObject mvjson = movieService.getmovieinfoselect(ticketing.getDateEntityTicket().getMovieEntityDate().getMvid());
            dto.setMovietitle(String.valueOf(mvjson.get("movieNm")));
            dto.setCnemaname(ticketing.getDateEntityTicket().getCnemaEntityDate().getCname());
            dto.setPrice(ticketing.getTprice());
            ticketdto.add(dto);
        }
            return ticketdto;
        }catch(Exception e) {
            return null;
        }

    }

    public int finddno(int tno){
        return ticketingRepository.findById(tno).get().getDateEntityTicket().getDno();
    }
    //자리개수 가져오기
    public int getseatcount(int tno,int type){
        try{
            JSONObject jsonObject = (JSONObject) jsonParser.parse(ticketingRepository.findById(tno).get().getTage());
            int result = 0;
            if(type==1){
                result =Integer.parseInt(String.valueOf(jsonObject.get("adult")));
            }else{
                result =Integer.parseInt(String.valueOf(jsonObject.get("youth"))) ;
            }
            return result;
        }catch(Exception e){}
        return 0;
    }

    @Autowired
    PaymentRepository paymentRepository;
    // 환불관리 리스트 출력
    public Page<PaymentEntity> paymentlist(Pageable pageable,String keyword, String search){
        int page=0;
        if(pageable.getPageNumber()==0){page=0;}    // 0이면 0페이지(기본페이지)
        else{page=pageable.getPageNumber()-1;}      // 1페이지 이상일때는 -1해서
        // 페이지 속성 페이지번호, 페이지당 게시물수, 정렬
        pageable=PageRequest.of(page,10,Sort.by(Sort.Direction.DESC,"pno"));
        // 검색이 있을경우
        if(keyword!=null&&keyword.equals("pmoviename")){return paymentRepository.findByPmoviename(search,pageable);}
        if(keyword!=null&&keyword.equals("tno")){return paymentRepository.findByTno(search,pageable);}
        if(keyword!=null&&keyword.equals("mid")){return paymentRepository.findByMid(search,pageable);}

        return paymentRepository.findAll(pageable);

    }

}
