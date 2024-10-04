package com.lewkowicz.cashflashapi.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class EmailAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            String requestEmail = context.getRequest().getParameter("email");
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> {
                        String authority = grantedAuthority.getAuthority();
                        return authority.equals("ADMIN") || authority.equals("ROLE_ADMIN");
                    });
            String jwtEmail = jwt.getClaimAsString("sub");
            return new AuthorizationDecision(isAdmin || (jwtEmail != null && jwtEmail.equals(requestEmail)));
        }
        return new AuthorizationDecision(false);
    }
}
