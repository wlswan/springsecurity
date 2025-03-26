package example.login.auth.oauth;

import example.login.auth.PrincipalDetails;
import example.login.auth.oauth.provider.GoogleUserUserInfo;
import example.login.auth.oauth.provider.NaverUserUserInfo;
import example.login.auth.oauth.provider.OAuth2UserInfo;
import example.login.entity.User;
import example.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService implements OAuth2UserService {

    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo = new GoogleUserUserInfo(oAuth2User.getAttributes());
        }
        else {
            oAuth2UserInfo = new NaverUserUserInfo((Map<String,Object>)oAuth2User.getAttributes().get("response"));
        }
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String email = oAuth2UserInfo.getEmail();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        else {
            user = User.builder()
                    .username(username)
                    .email(email)
                    .password(null)
                    .isVerified(true)
                    .provider(provider)
                    .providerId(providerId)
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user);
        }


        return new PrincipalDetails(user,oAuth2User.getAttributes());
    }
}
