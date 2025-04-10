package com.hescha.pet.questions.auth.security;

import com.hescha.pet.questions.auth.model.User;
import com.hescha.pet.questions.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            authHeader = authHeader.substring(7);
            try {
                String username = jwtTokenUtil.getUsername(authHeader);
                String tokenIdClaim = jwtTokenUtil.getTokenId(authHeader);

                User user = userService.loadUserByUsername(username);
                if (user != null && tokenIdClaim.equals(user.getCurrentTokenId())) {
                   user.setCurrentTokenId(null);
                   userService.update(user);
                }
            } catch (ExpiredJwtException ex) {
                log.debug(ex.getMessage());
            } catch (SignatureException ex) {
                log.warn("Signature exception: " + ex.getMessage());
            }
        }
    }
}
