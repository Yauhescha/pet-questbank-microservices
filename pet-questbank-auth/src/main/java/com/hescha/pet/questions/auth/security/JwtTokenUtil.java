package com.hescha.pet.questions.auth.security;

import com.hescha.pet.questions.auth.dto.TokenDto;
import com.hescha.pet.questions.auth.model.User;
import com.hescha.pet.questions.auth.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final String TOKEN_ACCESS = "access";
    private static final String TOKEN_REFRESH = "refresh";
    private static final String TOKEN_TYPE = "type";

    private final UserService userService;
    private final RedisCacheManager cacheManager;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token-lifetime}")
    private Duration accessTokenLifetime;
    @Value("${jwt.refresh-token-lifetime}")
    private Duration refreshTokenLifetime;


    public TokenDto generateToken(User user) {
        Pair<String, Date> accessToken = generateAccessToken(user);
        Pair<String, Date> refreshToken = generateRefreshToken(user);

        Objects.requireNonNull(cacheManager.getCache("users")).put(user.getUsername(), user.getCurrentTokenId());

        return TokenDto.builder()
                .accessToken(accessToken.getFirst())
                .accessExpiresIn(accessToken.getSecond())
                .refreshToken(refreshToken.getFirst())
                .refreshExpiresIn(refreshToken.getSecond())
                .build();
    }

    public Pair<String, Date> generateAccessToken(User user) {
        String tokenId = UUID.randomUUID().toString();
        user.setCurrentTokenId(tokenId);
        userService.update(user.getId(), user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", List.of());
        claims.put("type", "access");
        claims.put("tokenId", tokenId);

        return generateTokenDto(user, accessTokenLifetime, claims);
    }

    public Pair<String, Date> generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return generateTokenDto(user, refreshTokenLifetime, claims);
    }

    private Pair<String, Date> generateTokenDto(User user, Duration lifetime, Map<String, Object> claims) {
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + lifetime.toMillis());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return Pair.of(token, expiration);
    }

    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public String getTokenId(String token) {
        return (String) getAllClaims(token).get("tokenId");
    }

    public boolean isAccessToken(String token) {
        Claims claims = getAllClaims(token);
        return TOKEN_ACCESS.equals(claims.get("type"));
    }

    public boolean isRefreshToken(String token) {
        Claims claims = getAllClaims(token);
        return TOKEN_REFRESH.equals(claims.get(TOKEN_TYPE));
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
