package com.lewkowicz.cashflashapi.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return Optional.of(username);
        } else if (principal instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("email");
            return Optional.of(email);
        } else {
            String principalStr = principal.toString();
            String email = extractEmail(principalStr);
            return Optional.of(email);
        }
    }

    private String extractEmail(String principalStr) {
        int emailStart = principalStr.indexOf("email=") + 6;
        int emailEnd = principalStr.indexOf(',', emailStart);
        if (emailEnd == -1) {
            emailEnd = principalStr.indexOf('}', emailStart);
        }
        if (emailStart > 5 && emailEnd > emailStart) {
            return principalStr.substring(emailStart, emailEnd);
        } else if (emailStart > 5) {
            return principalStr.substring(emailStart);
        }
        return principalStr;
    }
}
