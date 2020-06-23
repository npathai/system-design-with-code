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

    private final String secret;

    public JWTCreator(String secret) {
        this.secret = secret;
    }

    public SignedJWT createJwtForRoot() throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        JWTClaimsSet payload = new JWTClaimsSet.Builder()
                .issuer("test-api")
                .subject("root")
                .claim("uid", "78e98faa-b502-11ea-a146-0242ac190002")
                .expirationTime(Date.from(Instant.now().plusSeconds(120)))
                .build();

        SignedJWT signedJWT = new SignedJWT(header, payload);
        MACSigner macSigner = new MACSigner(secret);
        signedJWT.sign(macSigner);
        return signedJWT;
    }
}
