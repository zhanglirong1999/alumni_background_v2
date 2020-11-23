package cn.edu.seu.alumni_background.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class TokenUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String buildToken(
        String tokenRawValue
    ) {
        return Jwts.builder().setSubject(tokenRawValue).signWith(key).compact();
    }
}
