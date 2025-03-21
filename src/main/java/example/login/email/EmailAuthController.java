package example.login.email;


import example.login.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class EmailAuthController {
    private final EmailService emailService;
    private final VerificationService verificationService;
    private final UserRepository userRepository;

    @PostMapping("/send-verification")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "이메일 형식이 아닙니다."));
        }
        //이메일 중복도 생각 나중에
        if(userRepository.existsByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("success", false, "message", "이미 가입된 이메일입니다."));
        }

        String verificationCode = verificationService.generateVerificationCode();
        verificationService.saveVerificationCode(email, verificationCode);
        emailService.sendVerificationEmail(email, verificationCode);

        return ResponseEntity.ok(Map.of("success", true, "message", "Verification code sent"));
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }


    @PostMapping("/verify-code")
    public ResponseEntity<Map<String,Object>> verifyCode(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        String code = request.get("code");
        boolean isValid = verificationService.verifyCode(email, code);
        if (isValid) {
            session.setAttribute("verifiedEmail",email);
            return ResponseEntity.ok(Map.of("success", true, "message", "인증 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "코드가 일치하지 않습니다."));
        }
    }

}
