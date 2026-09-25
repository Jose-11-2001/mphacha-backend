package mw.mphachayanga.auth;

import lombok.RequiredArgsConstructor;
import mw.mphachayanga.auth.dto.*;
import mw.mphachayanga.common.exception.ApiException;
import mw.mphachayanga.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ApiException("Email already registered", HttpStatus.CONFLICT);
        }
        User user = User.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .fullName(req.fullName())
                .phone(req.phone())
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);
        return toResponse(user);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED));
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        return new AuthResponse(
                jwtService.generateToken(user),
                user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
    }
}