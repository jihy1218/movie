package movie.service;

import movie.domain.Dto.MemberDto;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;


@Service
public class MemberService {
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



}//class end