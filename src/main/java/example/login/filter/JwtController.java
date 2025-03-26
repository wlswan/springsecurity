package example.login.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {
    @GetMapping("/api/token")
    public String getToken(Authentication authentication, HttpServletResponse response) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String token = JwtUtil.createToken(username);
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(true); // JavaScript 접근 방지
            jwtCookie.setPath("/"); // 모든 경로에서 사용
            jwtCookie.setMaxAge(60 * 60); // 1시간
            response.addCookie(jwtCookie);
            System.out.println("발급된 토큰 (쿠키): " + token); // 디버깅용
            return "토큰 발급 성공!";
        }
        return "로그인 먼저 해주세요.";
    }

    @GetMapping("/jwtTest")
    public String test() {
        return "API 테스트 성공!";
    }
}