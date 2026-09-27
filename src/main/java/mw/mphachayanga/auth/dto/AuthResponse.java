package mw.mphachayanga.auth.dto;
//update
public record AuthResponse(
        String token, Long userId, String email, String fullName, String role
) {}