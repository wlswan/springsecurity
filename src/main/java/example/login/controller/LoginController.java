package example.login.controller;

import example.login.auth.PrincipalDetails;
import example.login.entity.User;
import example.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;

    private final PasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }
    @GetMapping("/joinForm")
    public String join(){
        return "joinForm";
    }

    @GetMapping("/test/login")
    @ResponseBody
    //AuthenticationPrincipal로 세션정보를 확인할수있음 UserDetails 타입으로
    public String testLogin(Authentication authentication , @AuthenticationPrincipal PrincipalDetails userDetails){

        System.out.println("test/login------------");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails);
        System.out.println("username: " + userDetails.getUser());
        return "세션 정보 확인하기";

    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String testOauthLogin(Authentication authentication,
                                 @AuthenticationPrincipal OAuth2User oAuth){

        System.out.println("test/login------------");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication = " + oAuth2User.getAttributes());
        System.out.println(oAuth.getAttributes());
        return "oauth 세션 정보 확인하기";

    }
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println(principalDetails.getUser());
        return "유저창";
    }


    @PostMapping("/join")
    public String register(@RequestParam String username,@RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setRole("ROLE_USER");
        String encodePassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodePassword);
        userRepository.save(user);
        return "redirect:/loginForm";

    }
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public String info(){
        return "admin";
    }
    
}
