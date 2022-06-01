package br.edu.ufsj.tp.Helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JwtTokenHelper {
    private static final String secret = "secret";
    private static final Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));

    public static DecodedJWT verify(String token) throws JWTVerificationException {
        return JWT
                .require(algorithm)
                .build()
                .verify(token);
    }

    public static Map<String, String> signTokens(
            String subject,
            String url,
            String name,
            Collection<SimpleGrantedAuthority> authorities
    ) {
        String token = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(url)
                .withClaim("name", name)
                .withClaim(
                        "roles",
                        authorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                ).sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(url)
                .withClaim("name", name)
                .withClaim(
                        "roles",
                        authorities
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                ).sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", token);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public static Map<String, String> signTokens(
            String subject,
            String url,
            String name,
            List<String> authorities
    ) {
        String token = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(url)
                .withClaim("name", name)
                .withClaim(
                        "roles",
                        authorities
                ).sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(url)
                .withClaim("name", name)
                .withClaim(
                        "roles",
                        authorities
                ).sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", token);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }
}
