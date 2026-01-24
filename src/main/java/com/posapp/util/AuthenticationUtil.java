package com.posapp.util;

import org.springframework.security.core.Authentication;

public class AuthenticationUtil {

    public static Integer extractUserIdFromAuthentication(Authentication authentication) {
        // Extract user ID from JWT token claims
        // For now, returning a placeholder
        // In production, this would parse the JWT and extract the user ID
        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            // Username is extracted, but we need user ID
            // This requires storing user ID in JWT claims
            return 1; // Placeholder
        }

        return null;
    }

    public static String extractRoleFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("USER");
    }
}
