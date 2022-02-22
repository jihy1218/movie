package movie.service;

import movie.domain.Dto.CnemaDto;
import movie.domain.Dto.MemberDto;
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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

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

        //예약날짜 정보 가져오기
        DateEntity dateentity = dateRepository.findById(dno).get();
        //해당 날짜에 비활성화좌석목록 가져오기 (String상태 Json으로 변경해야함)
        String cnemaact = dateentity.getCnemaEntityDate().getCactive();
        //좌석크기 설정
        CnemaDto cnemaDto = new CnemaDto(20,10);
        System.out.println(cnemaDto.getCnemaact().toString());
        //최종 좌석 세팅값
        List<String> cnemaactlist = cnemaDto.getCnemaact();

        //JSONParser 선언
        JSONParser jsonParser = new JSONParser();
        try{
            /////////////////////////  좌석변경용  //////////////////////////////
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
            ///////////////////////// 좌석변경용 끝 //////////////////////////////

///////////////////////////////////////////////// 좌석배치 ////////////////////////////////////////////////////
            // 1.위에서 가져온 비활성화좌석목록 문자열 cnemaact 를 JSONObject로 변환
            JSONObject jsonObject = (JSONObject) jsonParser.parse(cnemaact);
            // 2.JSONObject를 ArrayList로 변환하여
            JSONArray jsonArray = (JSONArray) jsonObject.get("cnemalocation") ;
            // 3.비활성화 좌석을 담을 리스트 선언
            ArrayList<String> exceptlist = new ArrayList<>();
            // 4.비활성화 좌석을 다시 문자열으로 변환 후 리스트에 담기
            for(int i = 0; i<jsonArray.size(); i++){
                JSONObject exceptjson = (JSONObject)jsonArray.get(i);
                String   exceptString= (String) exceptjson.get("location");
                exceptlist.add(exceptString);
            }
            // 5. 비활성화된 좌석의 인덱스값을 리스트에 담기
            ArrayList<Integer> exceptIndexlist = new ArrayList<>();
            for(int i = 0; i< cnemaactlist.size();i++){
                for(String temp2 : exceptlist){
                    if(cnemaactlist.get(i).equals(temp2)){
                        exceptIndexlist.add(i);
                    }
                }
            }
///////////////////////////////////////////////// 좌석배치 끝 ////////////////////////////////////////////////////


///////////////////////////////////////////////// 예약된 좌석 비활성화 ////////////////////////////////////////////////////
            //해당 날짜에 예약정보 가져오기
            List<TicketingEntity> ticketing = dateentity.getTicketingEntities();
            //예약된 좌석 좌표리스트 선언
            ArrayList<String> ticketlist = new ArrayList<>();

            //예약좌석 제이슨 -> 리스트
            for(TicketingEntity temp : ticketing){
                JSONObject jsonObject1 = (JSONObject)jsonParser.parse(temp.getTseat());
                JSONArray jsonArray1 = (JSONArray) jsonObject1.get("tseat") ;
                for(int i = 0; i<jsonArray1.size(); i++){
                    JSONObject json = (JSONObject)jsonArray1.get(i);
                    String seatString= (String) json.get("seat");
                   // 가져온 예약정보 ticketing를 String으로 풀어서 담기
                    ticketlist.add(seatString);
                }
            }
///////////////////////////////////////////////// 예약된 좌석 비활성화  끝 ////////////////////////////////////////////////////

///////////////////////////////////////////////// 비활성화된 좌석 번호 넘기기 ////////////////////////////////////////////////
            // 1. 비활성 좌석의 인덱스값 리스트만큼
            for(Integer temp : exceptIndexlist){
                // a = 인덱스 번호
                int a = temp;
                // alength = 가로 좌석 번호
                int alength = a%20;
                // alength = 19 이면 오른쪽 끝좌석
                if(alength!=19){
                    // 밀어야할길이  = 가로길이(20) - 좌석 번호
                    int abc = 20-alength;
                    System.out.println("abc :"+abc);
                    // 한칸씩 밀기 i = 시작 인덱스번호 에서 abc()
                    for(int i =temp+1; i<temp+abc; i++){
                        // 좌석의 실제값
                        String value = cnemaactlist.get(i);
                        // value A,12
                        int ch = Integer.parseInt(value.split(",")[1])-1;
                        String fr = value.split(",")[0];
                        cnemaactlist.set(i,fr+","+ch);
                    }
                }
                cnemaactlist.set(temp,"X");
            }
            // 예약된 좌석 처리"#"
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

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    HttpServletRequest request;

    //티켓팅
    @Transactional
    public int ticketing(String tseat,String tage,String tprice,
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

        JSONObject jsonObject = movieService.getmovieinfoselect(dateentity.getMovieEntityDate().getMvid());

        HttpSession session = request.getSession();
        MemberDto memberDto= (MemberDto)session.getAttribute("logindto");
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .mid(memberDto.getMid())
                .pmoviename(String.valueOf(jsonObject.get("movieNm")))
                .ppeople(tage)
                .pprice(tprice)
                .pseat(tseat)
                .ptime(dateentity.getDdate()+"•"+dateentity.getDtime())
                .ptype("결제완료")
                .reviewact(1)
                .tno(ticketing.getTno())
                .build();
        paymentRepository.save(paymentEntity);
        return tno;
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

    //어드민예약취소
    @Transactional
    public boolean ticketcancel(int tno){

        TicketingEntity ticketing = ticketingRepository.findById(tno).get();
        JSONParser jsonParser = new JSONParser();
        try{
            JSONObject jsonObject = (JSONObject)jsonParser.parse(ticketing.getTage());
            int count = Integer.parseInt(String.valueOf(jsonObject.get("youth")))+Integer.parseInt(String.valueOf(jsonObject.get("adult")));
            ticketing.getDateEntityTicket().setDseat(ticketing.getDateEntityTicket().getDseat()+count);
        }catch(Exception e){}

        ticketingRepository.delete(ticketing);

        PaymentEntity paymentEntity = paymentRepository.findBytno(tno);
        paymentEntity.setPtype("환불요청");

        return true;
   }

    // 환불관리 리스트 출력
    public Page<PaymentEntity> paymentlist(Pageable pageable,String keyword, String search){
        int page=0;
        if(pageable.getPageNumber()==0){page=0;}    // 0이면 0페이지(기본페이지)
        else{page=pageable.getPageNumber()-1;}      // 1페이지 이상일때는 -1해서
        // 페이지 속성 페이지번호, 페이지당 게시물수, 정렬
        pageable=PageRequest.of(page,10,Sort.by(Sort.Direction.DESC,"pno"));

        // 검색이 있을경우
        if(keyword!=null&&keyword.equals("pmoviename")){
            Page<PaymentEntity> paymentEntity = replacePate(paymentRepository.findByPmoviename(search,pageable));
            return paymentEntity;
        }
        if(keyword!=null&&keyword.equals("tno")){
            Page<PaymentEntity> paymentEntity = replacePate(paymentRepository.findByTno(search,pageable));
            return paymentEntity;
        }
        if(keyword!=null&&keyword.equals("mid")){
            Page<PaymentEntity> paymentEntity = replacePate(paymentRepository.findByMid(search,pageable));
            return paymentEntity;
        }

        Page<PaymentEntity> paymentEntity = replacePate(paymentRepository.findAll(pageable));

        return paymentEntity;

    }

    //고객 결제내역
    public Page<PaymentEntity> memberpaymentmember(String mid , Pageable pageable){
        int page=0;
        if(pageable.getPageNumber()==0){page=0;}    // 0이면 0페이지(기본페이지)
        else{page=pageable.getPageNumber()-1;}      // 1페이지 이상일때는 -1해서
        // 페이지 속성 페이지번호, 페이지당 게시물수, 정렬
        pageable = PageRequest.of(page,10);
        Page<PaymentEntity> paymentEntity = replacePate(paymentRepository.findBymno(mid,pageable));
        return paymentEntity;
    }



    //페이지 가공
    public Page<PaymentEntity> replacePate(Page<PaymentEntity> page){
        List<PaymentEntity> list = page.getContent();
        try{
            for(int i=0; i<list.size(); i++){
                JSONObject jsonObject = (JSONObject)jsonParser.parse(list.get(i).getPpeople());
                String peple = "성인 :"+String.valueOf(jsonObject.get("adult"))+" 청소년 :"
                        +String.valueOf(jsonObject.get("youth")) ;
                list.get(i).setPpeople(peple);
                jsonObject = (JSONObject)jsonParser.parse(list.get(i).getPseat());
                JSONArray jsonArray = (JSONArray) jsonObject.get("tseat");
                String seat = "";
                for(int j=0; j<jsonArray.size();j++){
                    JSONObject jsonObject1 = (JSONObject)jsonArray.get(j);
                    if(j==0){
                        seat = String.valueOf(jsonObject1.get("seat")).replace(",","");
                    }else{
                        seat += ","+String.valueOf(jsonObject1.get("seat")).replace(",","");
                    }
                }
                list.get(i).setPseat(seat);
            }

        }catch (Exception e){}
        return page;
    }

    // 특정 결제번호 상태 변경하기
    @Transactional
    public boolean typeupdate(int pno, String ptype){
        PaymentEntity paymentEntity = paymentRepository.findById(pno).get();    // 해당 번호 결제 레코드 호출
        if(paymentEntity.getPtype().equals(ptype)) {    // 결제레코드의 타입과 같으면
            return false;   // 돌려보냄
        }else{
            paymentEntity.setPtype(ptype); return true; // 결제 상태 저장
        }
    }

    // 월 별 매출 가져오기
    public List<String> monthSales(String year){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        List<String> list = new ArrayList<>();
        int years = Integer.parseInt(year);
        for(int i=1; i<13;i++){
            String startday="";
            String endday="";
            date.setMonth(i);
            calendar.set(years,date.getMonth()-1, date.getDate());
            int firstday = calendar.getMinimum(Calendar.DAY_OF_MONTH);
            String firstday2 = "";
            if(firstday<10){
                firstday2 = "0"+firstday;
            }else{
                firstday2=firstday+"";
            }
            if(i<10){
                startday = years+"-"+"0"+i+"-"+firstday2;
            }else {
                startday = years + "-" + i + "-" + firstday2;
            }
            int lastday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if(i<10){
                endday = years+"-"+"0"+i+"-"+lastday;
            }else {
                endday = years + "-" + i + "-" + lastday;
            }
            List<PaymentEntity> paymentEntity = paymentRepository.monthSales(startday,endday);
            // 월 매출
            try{
                int result = 0;
                int price = 0;
                for(PaymentEntity temp : paymentEntity){
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(temp.getPpeople());
                    String adult = String.valueOf(jsonObject.get("adult")) ;
                    String youth = String.valueOf(jsonObject.get("youth")) ;
                    int adults = Integer.parseInt(adult);
                    int youths = Integer.parseInt(youth);
                    result += adults+youths;
                    price += Integer.parseInt(temp.getPprice());
                }
                    String ass = price+"@"+result;
                    list.add(ass);
            }catch (Exception e){  }
        }
        return list;
    }
    // tno로 예약내역가져오기
    public TicketDto getTicket(int tno){
        TicketingEntity ticketing = ticketingRepository.findById(tno).get();
        String date = ticketing.getDateEntityTicket().getDdate()+" "+ticketing.getDateEntityTicket().getDtime();
        try{
            JSONObject jsonObject = (JSONObject)jsonParser.parse(ticketing.getTseat());
            JSONArray jsonArray = (JSONArray) jsonObject.get("tseat");
            String seat="";
            for(int i=0; i<jsonArray.size();i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String sseat = (String)jsonObject1.get("seat");
                String line = sseat.split(",")[0];
                String num = sseat.split(",")[1];

                seat = seat + " "+line+"열"+num+"번";
            }
            JSONObject jsonObject1 = (JSONObject)jsonParser.parse(ticketing.getTage());
            JSONObject movienamejson = movieService.getmovieinfoselect(ticketing.getDateEntityTicket().getMovieEntityDate().getMvid());
            return TicketDto.builder()
                    .tno(tno)
                    .seat(seat)
                    .count("성인 : "+String.valueOf(jsonObject1.get("adult"))+" 청소년 : "+String.valueOf(jsonObject1.get("youth")))
                    .price(ticketing.getTprice())
                    .movietitle(String.valueOf(movienamejson.get("movieNm")))
                    .cnemaname(ticketing.getDateEntityTicket().getCnemaEntityDate().getCname())
                    .date(date)
                    .build();
        }catch (Exception e){ } return null;
    }





}
