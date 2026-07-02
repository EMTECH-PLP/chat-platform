package chatapp.chat_platform.auth.controller;

import chatapp.chat_platform.auth.dto.request.ResendVerificationRequest;
import chatapp.chat_platform.auth.dto.request.VerifyEmailRequest;
import chatapp.chat_platform.auth.dto.response.UserResponse;
import chatapp.chat_platform.auth.model.User;
import chatapp.chat_platform.auth.service.VerificationService;
import chatapp.chat_platform.auth.util.AuthConstants;
import chatapp.chat_platform.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Email Verification", description = "Email verification endpoints")
public class VerificationController {

    private final VerificationService verificationService;

    @Operation(
        summary = "Verify email",
        description = "Complete email verification using the token sent via email"
    )
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<?>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        User user = verificationService.verifyEmail(request.getToken());

        UserResponse response = new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );

        return ResponseEntity.ok(ApiResponse.ok(AuthConstants.MSG_EMAIL_VERIFIED, response));
    }

    @Operation(
        summary = "Resend verification email",
        description = "Resend the verification email to the user"
    )
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<?>> resendVerification(
            @Valid @RequestBody ResendVerificationRequest request) {
        verificationService.resendVerification(request.getEmail());
        return ResponseEntity.ok(ApiResponse.ok("Verification email resent successfully", null));
    }

    @Operation(
        summary = "Check email verification status",
        description = "Check if a user's email is verified"
    )
    @GetMapping("/email-verified/{userId}")
    public ResponseEntity<ApiResponse<?>> isEmailVerified(@PathVariable UUID userId) {
        boolean isVerified = verificationService.isEmailVerified(userId);
        return ResponseEntity.ok(ApiResponse.ok("Email verification status", isVerified));
    }
}