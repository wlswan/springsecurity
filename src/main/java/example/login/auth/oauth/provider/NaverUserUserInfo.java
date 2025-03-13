package example.login.auth.oauth.provider;

import java.util.Map;

public class NaverUserUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public NaverUserUserInfo(Map<String,Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getUsername() {
        return attributes.get("name").toString();
    }
}
