package example.login.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String to) {
        emailService.sendSimpleEmail(to, "테스트 이메일", "이것은 Spring Boot 메일 전송 테스트입니다.");
        return "이메일이 성공적으로 전송되었습니다.";
    }
}
