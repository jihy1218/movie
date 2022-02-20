package movie.domain.Dto;

import lombok.*;
import movie.domain.Entity.Member.MemberEntity;
import movie.domain.Entity.Member.Role;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MemberDto{
    private int mno;
    private String mid;
    private String mpassword;
    private String mname;
    private String msex;
    private String mphone;
    private String memail;
    private String mage;
    private String maddress;
    private String mgrade; // 회원등급


    //Dto --> entity
    //이름이 어드민일시 ADMIN 부여
    public MemberEntity toEntity(){
        if(mname.equals("admin")){
            return MemberEntity.builder()
                    .mid(this.mid)
                    .mpassword(this.mpassword)
                    .mname(this.mname)
                    .msex(this.msex)
                    .mphone(this.mphone)
                    .memail(this.memail)
                    .mage(this.mage)
                    .maddress(this.maddress)
                    .mgrade(Role.ADMIN)
                    .build();
        }else {

            return MemberEntity.builder()
                    .mid(this.mid)
                    .mpassword(this.mpassword)
                    .mname(this.mname)
                    .msex(this.msex)
                    .mphone(this.mphone)
                    .memail(this.memail)
                    .mage(this.mage)
                    .maddress(this.maddress)
                    .mgrade(Role.Member)
                    .build();
        }
    }





}
