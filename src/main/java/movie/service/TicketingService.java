package movie.service;

import movie.domain.Dto.CnemaDto;
import movie.domain.Entity.Cnema.CnemaRepository;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Date.DateRepository;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.MemberRepository;
import movie.domain.Entity.Movie.MovieRepository;
import movie.domain.Entity.Ticketing.TicketingEntity;
import movie.domain.Entity.Ticketing.TicketingRepository;
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
public class TicketingService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CnemaRepository cnemaRepository;

    @Autowired
    DateRepository dateRepository;

    @Autowired
    TicketingRepository ticketingRepository;

    public List<String> getseatlist(int dno){

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
            System.out.println("--------------ticketlist----------------");
            System.out.println("ticketlist :"+ticketlist);
            System.out.println("--------------ticketlist----------------");
            //예약좌석 제이슨 -> 리스트


            ArrayList<Integer> list = new ArrayList<>();
            for(int i = 0; i< cnemaactlist.size();i++){
                for(String temp2 : exceptlist){
                    if(cnemaactlist.get(i).equals(temp2)){
                        list.add(i);
                    }
                }
            }
            System.out.println(list.toString());
            // list = [0,9,19]
            for(Integer temp : list){
                System.out.println("temp :"+temp);
                int a = temp;
                int alength = a%20;
                System.out.println("값 :"+cnemaactlist.get(temp));
                System.out.println("alength :"+alength);
                if(alength!=19){
                    int abc =20-alength;
                    System.out.println("abc :"+abc);
                    for(int i =temp+1; i<temp+abc; i++){
                        String value = cnemaactlist.get(i);
                        int ch = Integer.parseInt(value.split(",")[1])-1;
                        String fr = value.split(",")[0];
                        cnemaactlist.set(i,fr+","+ch);
                        System.out.println("바뀐값 :"+cnemaactlist.get(i));
                    }
                }
                cnemaactlist.set(temp,"X");

            }
            for(int i =0; i<cnemaactlist.size(); i++){
                for(String temp2 : ticketlist){
                    if(cnemaactlist.get(i).equals(temp2)){
                        cnemaactlist.set(i,cnemaactlist.get(i)+"@");
                    }
                }
            }
            System.out.println(cnemaactlist.toString());

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


}
