package login;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.security.token.jwt.signature.secret.SecretSignatureConfiguration;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.UserDao;
import org.npathai.model.User;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@MicronautTest
public class LoginEndpointTest {

    @Inject
    @Client(value = "/")
    RxHttpClient httpClient;

    @Inject
    UserDao userDao;

    @Inject
    SecretSignatureConfiguration secretSignatureConfiguration;

    @Test
    public void returnsStatus200() throws DataAccessException {
        HttpResponse<BearerAccessRefreshToken> authenticationResponse = attemptLogin("root");

        assertThat(authenticationResponse.getStatus().getCode()).isEqualTo(HttpStatus.OK.getCode());
    }

    private HttpResponse<BearerAccessRefreshToken> attemptLogin(String password) throws DataAccessException {
        User user = createRootUser();
        when(userDao.getUserByName("root")).thenReturn(java.util.Optional.of(user));

        Flowable<HttpResponse<BearerAccessRefreshToken>> response = httpClient.exchange(
                HttpRequest.create(HttpMethod.POST, "/login")
                        .body(new LoginRequest("root", password)),
                BearerAccessRefreshToken.class);
        return response.blockingFirst();
    }

    @Test
    public void accessTokenContainsUID() throws ParseException, DataAccessException {
        HttpResponse<BearerAccessRefreshToken> authenticationResponse = attemptLogin("root");

        BearerAccessRefreshToken token = authenticationResponse.body();
        assertThat(token).isNotNull();

        JWT jwt = JWTParser.parse(token.getAccessToken());
        assertThat(jwt).isNotNull();
        JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
        assertThat(jwtClaimsSet.getClaim("uid").toString()).isNotBlank();
    }

    @Test
    public void returnsStatus401WhenUserCredentialsAreInvalid() {
        // TODO Can we improve this to check the HTTP status?
        assertThatThrownBy(() -> attemptInvalidLogin("root"))
                .isInstanceOf(HttpClientException.class);
    }

    private HttpResponse<Void> attemptInvalidLogin(String password) throws DataAccessException {
        when(userDao.getUserByName("root")).thenReturn(java.util.Optional.empty());

        Flowable<HttpResponse<Void>> response = httpClient.exchange(
                HttpRequest.create(HttpMethod.POST, "/login")
                        .body(new LoginRequest("root", password)),
                Void.class);
        return response.blockingSingle();
    }

    @NotNull
    private User createRootUser() {
        User user = new User();
        user.setUsername("root");
        // FIXME change to use of bcrypt library
        user.setPassword("$2a$09$C75xhHFSNwj0GV6STPWTqOgZ2qYpvH88QxGXbxWUF/kC0qgfJAEI.");
        user.setEmail("root@github.com");
        user.setId(UUID.randomUUID().toString());
        return user;
    }

    @MockBean(UserDao.class)
    public UserDao createUserDao() {
        return Mockito.mock(UserDao.class);
    }

    @SuppressWarnings("unused")
    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {

        }

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
