package example.login.repository;
import example.login.entity.PersistentLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RememberMeTokenRepository implements PersistentTokenRepository {
    private final PersistentLoginRepository rememberMeLoginRepository;
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin login = new PersistentLogin(
                token.getSeries(),
                token.getUsername(),
                token.getTokenValue(),
                token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
        rememberMeLoginRepository.save(login);

    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        Optional<PersistentLogin> findById = rememberMeLoginRepository.findById(series);
        if(findById.isPresent()) {
            PersistentLogin login = findById.get();
            login.setTokenValue(tokenValue);
            login.setLastUsed(lastUsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            rememberMeLoginRepository.save(login);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Optional<PersistentLogin> findById = rememberMeLoginRepository.findById(seriesId);
        if(findById.isPresent()) {
            PersistentLogin persistentLogin = findById.get();
            return new PersistentRememberMeToken(
                    persistentLogin.getUsername(),
                    persistentLogin.getSeries(),
                    persistentLogin.getTokenValue(),
                    Date.from(persistentLogin.getLastUsed().atZone(ZoneId.systemDefault()).toInstant())
            );
        }
        return null;
    }

    @Override
    @Transactional
    public void removeUserTokens(String username) {
        rememberMeLoginRepository.deleteByUsername(username);
    }
}
