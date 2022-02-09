package movie.domain.Entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum Role { // 맴버 등급 [권한]
    ADMIN("ROLE_ADMIN", "관리자"),
    Member("ROLE_MEMBER","일반회원");
    //생성자를 통한 키와 타이틀 할당

    private String key;
    private String title;



}
