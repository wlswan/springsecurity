package example.login.service;

import example.login.email.EmailService;
import example.login.entity.User;
import example.login.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Transactional
    public void registerUser(String email, String password) throws MessagingException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(true);
        user.generateVerificationToken();
        userRepository.save(user);
        emailService.sendVerificationEmail(email,user.getVerificationToken());
    }
}
