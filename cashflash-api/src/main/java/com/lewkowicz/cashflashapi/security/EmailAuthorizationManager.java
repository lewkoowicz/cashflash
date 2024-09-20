package com.lewkowicz.cashflashapi.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class EmailAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth != null) {
            String requestEmail = context.getRequest().getParameter("email");
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            if (auth.getPrincipal() instanceof Jwt jwt) {
                String jwtEmail = jwt.getClaimAsString("sub");
                return new AuthorizationDecision(isAdmin || (jwtEmail != null && jwtEmail.equals(requestEmail)));
            } else if (auth instanceof OAuth2AuthenticationToken) {
                String oauth2Email = ((OAuth2AuthenticationToken) auth).getPrincipal().getAttribute("email");
                return new AuthorizationDecision(isAdmin || (oauth2Email != null && oauth2Email.equals(requestEmail)));
            }
        }
        return new AuthorizationDecision(false);
    }
}
