package org.npathai.auth;

import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class BasicAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getIdentity().equals("root") && authenticationRequest.getSecret().equals("root")) {
            return Flowable.just(new UserDetails("root", new ArrayList<>()));
        }
        return Flowable.just(new AuthenticationFailed());
    }
}
