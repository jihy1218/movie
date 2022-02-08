package movie.service;

import movie.domain.Dto.IntergratedDto;
import movie.domain.Dto.MemberDto;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;



@Service
public class MemberService implements UserDetailsService {
    @Autowired
    MemberRepository memberRepository;


    //회원등록 메소드
    public boolean membersignup(MemberDto memberDto) {
        memberRepository.save(memberDto.toEntity());
        return true;
    }

    //회원 아이디 중복체크
    public boolean idcheck(String mid) {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for(MemberEntity memberEntity :memberEntities){
            if(memberEntity.getMid().equals(mid)){
                return true;//중복
            }
        }
        return false;//중복없음
    }
    //이메일 중복체크
    public boolean emailcheck(String eamil){
        List<MemberEntity>memberEntities = memberRepository.findAll();
        for(MemberEntity memberEntity : memberEntities){
            if(memberEntity.getMemail().equals(eamil)){
                return true;
            }
        }
            return false;
    }
   /* //회원 로그인 메소드
    public MemberDto login(MemberDto memberDto){
        List<MemberEntity>memberEntityList = memberRepository.findAll();
        for(MemberEntity memberEntity : memberEntityList){
            if(memberEntity.getMid().equals(memberDto.getMid())&&memberEntity.getMpassword().equals(memberDto.getMpassword())){
                return MemberDto.builder().mid(memberEntity.getMid()).mno(memberEntity.getMno()).build();
            }
        }
        return null;

    }*/

    @Autowired
    private HttpServletRequest request;
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        //회원 아이디로 회원 엔티티 찾기
        Optional<MemberEntity> entityOptional = memberRepository.findBymid(mid);
        MemberEntity memberEntity = entityOptional.orElse(null);
        //orElse(null)만약에 엔티티 가 없으면 null
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(memberEntity.getRoleKey()));
        //세션부여
        MemberDto loginDto = MemberDto.builder().mid(memberEntity.getMid()).mno(memberEntity.getMno()).build();

    }
    // 회원 정보 불러오기 메소드 ( 진행중 지형 )
    public MemberDto getMemberDto(int mno){
        Optional<MemberEntity> memberEntity = memberRepository.findById(mno);
        return MemberDto.builder()
                .mid(memberEntity.get().getMid())
                .mname(memberEntity.get().getMname())
                .memail(memberEntity.get().getMemail())
                .maddress(memberEntity.get().getMaddress())
                .msex(memberEntity.get().getMsex())
                .mphone(memberEntity.get().getMphone())
                .mage(memberEntity.get().getMage())
                .mpassword(memberEntity.get().getMpassword())
                .build();
    }
    // 아이디 찾기 메소드
    public String findid(String name, String email){
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for(MemberEntity memberEntity : memberEntities){
            if(memberEntity.getMname().equals(name)&&memberEntity.getMemail().equals(email)){
                return memberEntity.getMid();
            }
        }
        return null;
    }
    // 비밀번호 찾기 메일 전송 메소드
    @Autowired
    private JavaMailSender javaMailSender;
    @Transactional //
    public boolean findpassword(String id, String email){
        List<MemberEntity> memberEntities = memberRepository.findAll();
        for(MemberEntity memberEntity : memberEntities){
            if(memberEntity.getMid().equals(id)&&memberEntity.getMemail().equals(email)){
                // 아이디와 이메일이 DB에 존재하면
                StringBuilder builder = new StringBuilder();
                // StringBuilder : 문자열 연결 클래스
                builder.append("<html><body><h3>임시비밀번호</h3>");
                // 임시 비밀번호 난수 생성
                Random random = new Random();
                StringBuilder temppassword = new StringBuilder();
                for(int i=0; i<10; i++){
                    // 랜덤 숫자 -> 문자변환 [ 문자마다 번호가존재 ]
                    temppassword.append((char)((int)(random.nextInt(26))+97));
                }
                builder.append("<div>"+temppassword+"</div><br><h3 style='color : red;'>받으신후 비밀번호를 변경해 주세요!!</h3></body></html>");
                memberEntity.setMpassword(temppassword.toString()); // 랜덤 난수로 비밀번호 변경
                try{
                    MimeMessage message = javaMailSender.createMimeMessage();
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true,"UTF-8");
                    mimeMessageHelper.setFrom("kimji1218@naver.com","Movie"); // 보내는 사람,  이름
                    mimeMessageHelper.setTo(memberEntity.getMemail()); // 받는 사람 메일주소
                    mimeMessageHelper.setSubject("임시비밀번호 발송입니다.");  // 메일 제목
                    mimeMessageHelper.setText(builder.toString(),true);     // 메일 형식
                    javaMailSender.send(message);
                }catch (Exception e){}
                    return  true;
            }
        }
        return  false;
    }
    // 회원 정보 수정하기
     @Transactional
    public boolean infoupdate(int mno,String temp,int type){
        Optional<MemberEntity> memberEntities = memberRepository.findById(mno);
        if(type==1){memberEntities.get().setMpassword(temp); return true;} // 비밀번호 변경
        if(type == 2){memberEntities.get().setMphone(temp); return  true;} // 핸드폰 번호 변경
        if(type==3){memberEntities.get().setMaddress(temp);return true;} // 주소 변경
        return false;
     }
            HttpSession session = request.getSession();
            session.setAttribute("logindto",loginDto);

       //회원정보와 권한을 갖는 UserDetails 반환
        return new IntergratedDto(memberEntity, authorities);
    }
}//class end