package movie.service;

import movie.domain.Dto.CnemaDto;
import movie.domain.Entity.Cnema.CnemaRepository;
import movie.domain.Entity.Date.DateEntity;
import movie.domain.Entity.Date.DateRepository;
import movie.domain.Entity.Movie.MovieRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    public List<String> getseatlist(){
        //date 가져오기
        DateEntity dateentity = dateRepository.findById(12).get();
        String cnemaact = dateentity.getCnemaEntityDate().getCactive();
        //좌석 가져오기
        CnemaDto cnemaDto = new CnemaDto(20,10);
        List<String> cnemaactlist = cnemaDto.getCnemaact();
        //json으로 바꾸기
        JSONParser jsonParser = new JSONParser();
        try{
            JSONObject jsonObject = (JSONObject) jsonParser.parse(cnemaact);
            JSONArray jsonArray = (JSONArray) jsonObject.get("cnemalocation") ;
            ArrayList<String> exceptlist = new ArrayList<>();

            for(int i = 0; i<jsonArray.size(); i++){
                JSONObject exceptjson = (JSONObject)jsonArray.get(i);
                String   exceptString= (String) exceptjson.get("location");
                exceptlist.add(exceptString);
            }
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
                int a = temp;
                int alength = a%20;
                System.out.println("값 :"+cnemaactlist.get(temp));
                System.out.println("alength :"+alength);
                if(alength!=0){
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
                //for(int i = temp; i<10; i++)
                cnemaactlist.set(temp,"X");
            }

//            for(Integer temp : list){
//                System.out.println("temp :"+temp);
//                System.out.println("값 :"+cnemaactlist.get(temp));
//                String a = cnemaactlist.get(temp).split(",")[1];
//                System.out.println("a :" + a);
//                int alength = Integer.parseInt(a)%10;
//                System.out.println("alength :" + alength);
//                int abc = 10-alength;
//                System.out.println("abc :" + abc);
//                for(int i=temp+1; i<temp+abc+1; i++){
//                    String value = cnemaactlist.get(i);
//                    int ch = Integer.parseInt(value.split(",")[1])-1;
//                    String fr = value.split(",")[0];
//                    //System.out.println("value :"+value+"| fr :"+fr+"|  ch :"+ch+" | temp+abc:"+i+abc);
//                    cnemaactlist.set(i,fr+","+ch);
//
//                }
//                cnemaactlist.set(temp,"X");
//            }


        }catch (Exception e){

        }
        return cnemaactlist;
    }




}
