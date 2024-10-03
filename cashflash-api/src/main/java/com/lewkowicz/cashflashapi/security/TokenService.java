package com.lewkowicz.cashflashapi.security;

import com.lewkowicz.cashflashapi.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;
    private final UserRepository userRepository;

    public void generateTokenAndSetCookie(Authentication authentication, HttpServletResponse response) {
        String token = generateToken(authentication);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        String email = authentication.getName();
        String userId = fetchUserIdByEmail(email);

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .subject(email)
                .claim("scope", authorities)
                .claim("userId", userId);

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            String provider = oauthToken.getAuthorizedClientRegistrationId();
            claimsBuilder.claim("provider", provider);
        }

        JwtClaimsSet claims = claimsBuilder.build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String fetchUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> String.valueOf(user.getUserId()))
                .orElse(null);
    }

}
