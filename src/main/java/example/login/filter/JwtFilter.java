package example.login.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("JwtFilter 호출됨 - 경로: " + path);

        if (path.equals("/login") || path.startsWith("/oauth2/") || path.equals("/api/token") ||
                path.equals("/loginForm") || path.equals("/joinForm") || path.equals("/join") ||
                path.equals("/auth/send-verification") || path.equals("/auth/verify-code") ||
                path.equals("/")) {
            System.out.println("JWT 체크 건너뜀: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/jwtTest")) {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                SecurityContextHolder.clearContext();
            }

            String token = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        System.out.println("쿠키에서 읽은 토큰: " + token);
                        break;
                    }
                }
            }

            if (token != null && JwtUtil.validateToken(token)) {
                String username = JwtUtil.getUsername(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("JWT 인증 성공: " + username);
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다!");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

//    if (path.startsWith("/jwtTest")) {
//        System.out.println("JWT 체크 시작: " + path);
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            System.out.println("세션 지움");
//            SecurityContextHolder.clearContext();
//        }
//
//        String authHeader = request.getHeader("Authorization");
//        System.out.println("Authorization 헤더: " + authHeader);
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            if (JwtUtil.validateToken(token)) {
//                String username = JwtUtil.getUsername(token);
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);
//                SecurityContextHolder.getContext().setAuthentication(auth);
//                System.out.println("JWT 인증 성공: " + username);
//                filterChain.doFilter(request, response);
//            } else {
//                System.out.println("유효하지 않은 토큰");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
//                return;
//            }
//        } else {
//            System.out.println("JWT 토큰 없음");
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다!");
//            return;
//        }
//    } else {
//        System.out.println("JWT 필요 없는 경로: " + path);
//        filterChain.doFilter(request, response);
//    }
//}
}