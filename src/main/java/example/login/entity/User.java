package example.login.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
public class User {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) //oauth2로그인 할때는 ex)google_~~ , 일반 로그인폼으로는 이메일
    private String username;

    @Column(unique = true) //이메일
    private String email;

    private String password;

    private String role;

    private String provider;

    private String providerId;

    private boolean isVerified = false;

    private String verificationToken;

    public void generateVerificationToken() {
        this.verificationToken = UUID.randomUUID().toString();
    }



}
