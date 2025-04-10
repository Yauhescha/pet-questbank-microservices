package com.hescha.pet.questions.auth.security;

import com.hescha.pet.questions.auth.model.User;
import com.hescha.pet.questions.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            authHeader = authHeader.substring(7);
            try {
                // Проверяем, что это access token, а не refresh token
                if (jwtTokenUtil.isAccessToken(authHeader)) {
                    username = jwtTokenUtil.getUsername(authHeader);
                    String tokenIdClaim = jwtTokenUtil.getTokenId(authHeader);

                    User user = userService.loadUserByUsername(username);
                    if (user == null || !tokenIdClaim.equals(user.getCurrentTokenId())) {
                        log.debug("Invalid token: token id mismatch");
                        // Отказываем в доступе
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                        return;
                    }
                } else {
                    log.debug("Received refresh token in protected resource request");
                }
            } catch (ExpiredJwtException ex) {
                log.debug(ex.getMessage());
            } catch (SignatureException ex) {
                log.warn("Signature exception: " + ex.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
