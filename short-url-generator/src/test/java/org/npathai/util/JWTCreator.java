package org.npathai.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.time.Instant;
import java.util.Date;

public class JWTCreator {

    public static final String USER_ID = "78e98faa-b502-11ea-a146-0242ac190002";
    public static final String USER_NAME = "root";
    private final String secret;

    public JWTCreator(String secret) {
        this.secret = secret;
    }

    public SignedJWT createJwtForRoot() throws JOSEException {
        return createJwtFor(USER_ID, USER_NAME);
    }

    public SignedJWT createJwtFor(String uid, String username) throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        JWTClaimsSet payload = new JWTClaimsSet.Builder()
                .issuer("test-api")
                .subject(USER_NAME)
                .claim("uid", USER_ID)
                .expirationTime(Date.from(Instant.now().plusSeconds(120)))
                .build();

        SignedJWT signedJWT = new SignedJWT(header, payload);
        MACSigner macSigner = new MACSigner(secret);
        signedJWT.sign(macSigner);
        return signedJWT;
    }
}
