package com.hescha.pet.questions.auth.controller;

import com.hescha.pet.questions.auth.dto.TokenDto;
import com.hescha.pet.questions.auth.model.User;
import com.hescha.pet.questions.auth.security.JwtTokenUtil;
import com.hescha.pet.questions.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager manager;

    @PostMapping("/login")
    public ResponseEntity<?> getToken(
            @RequestParam String username,
            @RequestParam String password) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("BadCredentials", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.loadUserByUsername(username);
        TokenDto token = jwtTokenUtil.generateToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        try {
            if (!jwtTokenUtil.isRefreshToken(refreshToken)) {
                return new ResponseEntity<>("Invalid token type", HttpStatus.UNAUTHORIZED);
            }
            String username = jwtTokenUtil.getUsername(refreshToken);
            User user = userService.loadUserByUsername(username);
            TokenDto token = jwtTokenUtil.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/registration")
    public User registrationPost(
            @RequestParam String username,
            @RequestParam String password) {
        return userService.register(username, password);
    }

    @GetMapping("/user")
    public String getUser(Principal principal) {
        return principal.getName();
    }
}
