package movie.config;



import movie.service.MemberService;
import movie.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration//설정 클래스 설정
@EnableWebSecurity //시큐리티
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()  //Url 인증 요청
              //.antMatchers("/admin/**").hasRole("ADMIN")
              .antMatchers("/member/info").hasRole("Member") // 로그인 한사람 내정보들어갈수있음
              .antMatchers("/member/**").permitAll()        //비회원 로그인 위에거 제외하고 다들어갈수있음
              .antMatchers("/**").permitAll()
              .and()
              .csrf()
              .ignoringAntMatchers("/**")
              .and()
              .formLogin()
              .loginPage("/member/login")
              .loginProcessingUrl("/member/logincontroller")
              .defaultSuccessUrl("/")
              .usernameParameter("mid")
              .passwordParameter("mpassword")

              .and()
              .logout()
              .logoutRequestMatcher( new AntPathRequestMatcher("/member/logout"))
              .logoutSuccessUrl("/")
              .invalidateHttpSession(true)
              .and()
              .exceptionHandling()
              .accessDeniedPage("/error")
              .and()
              .oauth2Login()
              .userInfoEndpoint()
              .userService(oauthService);
            }
    @Autowired
    private OauthService oauthService;
    @Autowired
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Autowired
    private MemberService memberService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }


}
