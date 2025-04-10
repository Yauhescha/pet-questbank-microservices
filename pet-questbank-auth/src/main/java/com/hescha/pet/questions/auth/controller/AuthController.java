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
        User user = userService.loadUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
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
    public ResponseEntity<?> registrationPost(
            @RequestParam String username,
            @RequestParam String password) {
        if (userService.loadUserByUsername(username) != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }
        User user = userService.register(username, password);
        TokenDto token = jwtTokenUtil.generateToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/user")
    public String getUser(Principal principal) {
        return principal.getName();
    }
}
