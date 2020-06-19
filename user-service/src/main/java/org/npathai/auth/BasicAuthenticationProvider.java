package org.npathai.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.UserDao;
import org.npathai.model.User;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Optional;

import static org.apache.logging.log4j.LogManager.getLogger;

@Singleton
public class BasicAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOG = getLogger(BasicAuthenticationProvider.class);

    private final UserDao userDao;

    public BasicAuthenticationProvider(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        LOG.debug("Authentication request received for user: {}", authenticationRequest.getIdentity());

        Optional<User> optionalUser;
        try {
            optionalUser = userDao.getUserByName(String.valueOf(authenticationRequest.getIdentity()));
        } catch (DataAccessException ex) {
            // FIXME can we handle this gracefully?
            LOG.trace(ex);
            return Flowable.just(new AuthenticationFailed());
        }

        if (optionalUser.isEmpty()) {
            return Flowable.just(new AuthenticationFailed());
        }

        User user = optionalUser.get();
        if (validateCredentials(authenticationRequest, user)) {
            return Flowable.just(new UserDetails(user.getUsername(), new ArrayList<>()));
        }
        return Flowable.just(new AuthenticationFailed());
    }

    /**
     * Uses bcrypt cryptographic hash for calculating and validating password hash
     */
    private boolean validateCredentials(AuthenticationRequest authenticationRequest, User user) {
        return BCrypt.verifyer().verify(String.valueOf(authenticationRequest.getSecret()).toCharArray(),
                user.getPassword()).verified;
    }
}
