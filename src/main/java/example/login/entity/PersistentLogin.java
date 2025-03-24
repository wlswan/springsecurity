package example.login.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersistentLogin {
    @Id
    private String series; //토큰 구별 아이디

    private String username;

    private String tokenValue; //토큰에서 쓰는 값

    private LocalDateTime lastUsed;
}
