package movie.domain.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import movie.domain.Entity.Member.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
public class IntergratedDto implements UserDetails {
    //일반회원+oauth[소설계정]=>통합 dto
    private int mno;
    private String mid;
    private String mpassword;
    private final Set<GrantedAuthority> authorities;//인증
    //생성자
    public IntergratedDto(MemberEntity memberEntity , java.util.Collection<? extends GrantedAuthority> authorities ){
        this.mid = memberEntity.getMid();
        this.mpassword = memberEntity.getMpassword();
        this.mno = memberEntity.getMno();
        this.authorities = Collections.unmodifiableSet( new LinkedHashSet<>( this.sortAuthorities(authorities) ) );
    }


    private Set<GrantedAuthority>sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortAuthorities = new TreeSet<>(Comparator.comparing(GrantedAuthority::getAuthority));
        return sortAuthorities;
    }
    @Override //회원의 패스워드 반환 메소드
    public String getPassword() {
        return this.mpassword;
    }

    @Override// 회원의 id[이름]반환 메소드
    public String getUsername() {
        return this.mid;
    }

    @Override//계정이 만료 여부 확인[true : 만료되지 않음]
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override//계정이 잠겨 있는지 확인[true: 잠겨있지 않음]
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //계정의 패스워드가 만료 여부 확인[true: 만료되지 않음]
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override//계정이 사용가능한 계정인지 확인[true: 사용가능]
    public boolean isEnabled() {
        return true;
    }
}
