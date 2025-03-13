package example.login.auth.oauth.provider;

import java.util.Map;

public class GoogleUserUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public GoogleUserUserInfo(Map<String,Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "google";
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
