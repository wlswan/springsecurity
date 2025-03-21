package example.login.email;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final StringRedisTemplate redisTemplate;
    private static final int EXPIRATION_TIME = 5 * 60; // 초단위

    public String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 랜덤 코드 생성
    }

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(code);
    }
}

