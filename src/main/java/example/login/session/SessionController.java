package example.login.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SessionController {

    @GetMapping("/session-details")
    public Map<String, Object> getSessionDetails(HttpSession session) {
        Map<String, Object> sessionAttributes = new HashMap<>();
        Enumeration<String> attributeNames = session.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);

            // JSON 변환이 가능한 경우만 추가 (UserDetails는 직접 반환하지 않음)
            if (attributeValue instanceof String || attributeValue instanceof Number || attributeValue instanceof Boolean) {
                sessionAttributes.put(attributeName, attributeValue);
            } else {
                sessionAttributes.put(attributeName, attributeValue.toString()); // 객체를 문자열로 변환
            }
        }

        // SecurityContext에서 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", userDetails.getUsername()); // 이메일 반환
            userInfo.put("authorities", userDetails.getAuthorities());
            sessionAttributes.put("user", userInfo);
        }

        return sessionAttributes;
    }
}
