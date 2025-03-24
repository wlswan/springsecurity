package example.login.config;

import example.login.auth.PrincipalDetailsService;
import example.login.auth.oauth.PrincipalOauth2UserService;
import example.login.repository.RememberMeTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = true) // 각각 secured preAuthorized 어노테이션 쓸수있음
@RequiredArgsConstructor
public class SecurityConfig {


    private final PrincipalOauth2UserService principalOauth2UserService;
    private final PrincipalDetailsService principalDetailsService;
    private final RememberMeTokenRepository rememberMeTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(auth-> auth.disable());


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login","/join", "/loginForm","/joinForm").permitAll()
                        .requestMatchers("/auth/send-verification", "/auth/verify-code").permitAll() // 인증 없이 접근 허용
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated());

        http.formLogin((auth) -> auth
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .permitAll());

        http.oauth2Login((oAuth2)-> oAuth2
                .loginPage("/loginForm")
                .defaultSuccessUrl("/")
                .userInfoEndpoint((userInfo)->userInfo
                        .userService(principalOauth2UserService)));


        http.rememberMe((remember) -> remember
                .key("myseretkey")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(14*24*60*60)
                .tokenRepository(rememberMeTokenRepository)
                .userDetailsService(principalDetailsService));

        http.logout((logout)->logout
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID","remember-me")
                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
