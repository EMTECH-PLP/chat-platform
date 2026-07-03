package chatapp.chat_platform.auth.controller;

import chatapp.chat_platform.auth.dto.ChangePasswordRequest;
import chatapp.chat_platform.auth.dto.ForgotPasswordRequest;
import chatapp.chat_platform.auth.dto.ResetPasswordRequest;
import chatapp.chat_platform.auth.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordService.processForgotPassword(request);
        return ResponseEntity.ok(Map.of("message", "Reset token generated and dispatched successfully. Check server console logs."));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordService.processResetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Password has been successfully reset."));
    }

    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Authentication context missing. Bearer token required."));
        }
        passwordService.processChangePassword(principal.getName(), request);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
    }
}