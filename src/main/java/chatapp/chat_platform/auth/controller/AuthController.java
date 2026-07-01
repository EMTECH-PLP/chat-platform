package chatapp.chat_platform.auth.controller;

import chatapp.chat_platform.auth.dto.LoginRequest;
import chatapp.chat_platform.auth.dto.RegisterRequest;
import chatapp.chat_platform.auth.service.AuthService;
import chatapp.chat_platform.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
        var user = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("User registered successfully", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request) {
        var user = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", user));
    }
}
