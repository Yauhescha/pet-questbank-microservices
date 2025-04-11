package com.hescha.pet.questions.questbank.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final RedisCacheManager cacheManager;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // Пропускаем запросы к открытым ресурсам
        String path = request.getURI().getPath();
        if ( path.startsWith("/auth/login")
                || path.startsWith("/auth/registration")
                || path.startsWith("/auth/refresh") ) {
            return chain.filter(exchange);
        }

        // Получаем заголовок Authorization
        String authHeader = request.getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return verifyToken(exchange, chain, authHeader);
        }

        // Если заголовок не найден или не соответствует формату, возвращаем 401
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> verifyToken(ServerWebExchange exchange, GatewayFilterChain chain, String authHeader) {
        Claims claims;
        String token = authHeader.substring(BEARER_PREFIX.length());
        try {
            // проверка JWT
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            log.debug("Error parsing JWT", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (!"access".equals(claims.get("type"))) {
            log.debug("Invalid token type. Expected 'access' but found '{}'", claims.get("type"));
            exchange.getResponse().setStatusCode(HttpStatus.CONFLICT);
            return exchange.getResponse().setComplete();
        }

        String tokenId = claims.get("tokenId", String.class);
        String tokenIdFromCache = getUserTokenIdFromCache(claims.getSubject());
        if (!tokenId.equals(tokenIdFromCache)) {
            log.debug("Invalid token id for user '{}'. Expected '{}', actual '{}'",
                    claims.getSubject(), tokenIdFromCache, tokenId);
            exchange.getResponse().setStatusCode(HttpStatus.GONE);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    // Определяем приоритет выполнения фильтра. Чем ниже число, тем раньше срабатывает фильтр.
    @Override
    public int getOrder() {
        return -1;
    }

    public String getUserTokenIdFromCache(String username) {
        return Objects.requireNonNull(cacheManager.getCache("users")).get(username, String.class);
    }
}
