package movie.domain.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.Role;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Getter @Setter
public class Oauth2Dto {
//-----------------------추가하기
    //이름
    String name;
    //이메일
    String email;
    // 성별
    String sex;
    // 나이대
    String age;
    // 핸드폰
    String phone;

    // 회원정보
    private Map<String , Object> attribute;
    //요청 토큰[키]
    private String nameattributekey;




    //풀생성자
    @Builder
    public Oauth2Dto(String name, String email,String sex,String age,String phone, Map<String, Object> attribute, String nameattributekey) { //-----------------------추가하기
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.age = age;
        this.phone=phone;
        this.attribute = attribute;
        this.nameattributekey = nameattributekey;
    }
    //클라이언트 구분용 메소드 [카카오 네이버 구글]
    public static Oauth2Dto of (String registrationid, String nameattributekey , Map<String,Object>attribute){
        if( registrationid.equals("kakao")){   return ofkakao(    nameattributekey , attribute  ); }
        else if(registrationid.equals("naver")){return ofnaver(nameattributekey,attribute);}
        else{return ofgoogle(nameattributekey,attribute);}
    }

    //카카오 정보 dto 변환 메소드
    private static Oauth2Dto ofkakao(String nameattributekey,Map<String,Object>attribute){

        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String,Object>kakao_account =(Map<String,Object>) attribute.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String,Object>profile = (Map<String,Object>) kakao_account.get("profile");

        return Oauth2Dto.builder()//-----------------------추가하기
                .name((String)profile.get("nickname")) // name 이거 우리가 정하는거아님
                .email((String) kakao_account.get("email"))
                .sex((String) kakao_account.get("gender"))
                .age((String) kakao_account.get("age_range"))
                .attribute(attribute)
                .nameattributekey(nameattributekey)
                .build();
    }

    // 네이버 정보 dto 변환 메소드
    private static Oauth2Dto ofnaver( String nameattributekey ,Map<String, Object> attribute  ){

        Map<String, Object> response = (Map<String, Object>) attribute.get("response"); // response 키 호출

        return Oauth2Dto.builder()
                .name( (String) response.get("name") )
                .email((String) response.get("email"))
                .phone((String) response.get("mobile"))
                .attribute( attribute )
                .nameattributekey( nameattributekey )
                .build();
    }
    // 구글 정보 dto 변환 메소드
    private static Oauth2Dto ofgoogle( String nameattributekey ,Map<String, Object> attribute  ){

        return Oauth2Dto.builder()
                .name( (String) attribute.get("name") ) //구글 회원이름// name 이거 우리가 정하는거아님
                .email((String) attribute.get("email")) //구글 회원 이메일
                .attribute( attribute ) // 구글 회원정보
                .nameattributekey( nameattributekey )   //구글 인증키
                .build();
    }

    @Autowired
    MemberDto memberDto;

    // 첫 로그인했을때 회원가입 dto -> entitiy
    public MemberEntity toEntity(){

        return MemberEntity.builder().mname(name).memail(email).msex(sex).mage(age).mphone(phone).mgrade(Role.Member).mid(email.split("@")[0]).build();
        //-----------------------추가하기
    }

}
