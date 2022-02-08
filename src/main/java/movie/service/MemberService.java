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
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

            HttpSession session = request.getSession();
            session.setAttribute("logindto",loginDto);

       //회원정보와 권한을 갖는 UserDetails 반환
        return new IntergratedDto(memberEntity, authorities);
    }
}//class end