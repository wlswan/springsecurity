package example.login.auth;

import example.login.entity.User;
import example.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//securityconfig에서 logniproceesingUrl("/login")
//login 요청이 오면 자동으로 UserDetailsService 타입으로 ioc되어 있는 loadUserByUsername이거 실행됨
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    //로그인 폼에서 username으로 적어야 받아짐
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("유저 이름이 존재하지 않습니다"+username));

        return new PrincipalDetails(user);
    }
    //이때 Authentication 객체 안으로 들어감

}
