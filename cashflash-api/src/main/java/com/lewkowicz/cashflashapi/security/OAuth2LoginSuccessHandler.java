package com.lewkowicz.cashflashapi.security;

import com.lewkowicz.cashflashapi.entity.User;
import com.lewkowicz.cashflashapi.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken)) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        if (!"google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");

        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> handleExistingUser(response, oAuth2AuthenticationToken, principal, user),
                        () -> handleNewUser(response, email)
                );

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void handleExistingUser(HttpServletResponse response, OAuth2AuthenticationToken oAuth2AuthenticationToken, DefaultOAuth2User principal, User user) {
        Authentication newAuth = createNewAuthentication(oAuth2AuthenticationToken, principal, user);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        String jwt = tokenService.generateToken(newAuth);
        redirectToFrontend(response, "?token=" + jwt);
    }

    private Authentication createNewAuthentication(OAuth2AuthenticationToken oAuth2AuthenticationToken, DefaultOAuth2User principal, User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        DefaultOAuth2User newUser = new DefaultOAuth2User(
                List.of(authority),
                principal.getAttributes(),
                "email"
        );
        return new OAuth2AuthenticationToken(
                newUser,
                List.of(authority),
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
        );
    }

    private void handleNewUser(HttpServletResponse response, String email) {
        User newUser = new User();
        newUser.setRole("USER");
        newUser.setEmail(email);
        newUser.setPassword("");
        userRepository.save(newUser);

        redirectToFrontend(response, "?token=oauth");
    }

    private void redirectToFrontend(HttpServletResponse response, String queryParams) {
        try {
            response.sendRedirect(frontendUrl + queryParams);
        } catch (IOException e) {
            throw new RuntimeException("Failed to redirect to frontend", e);
        }
    }
}
