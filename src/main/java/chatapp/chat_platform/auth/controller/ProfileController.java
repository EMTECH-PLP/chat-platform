package chatapp.chat_platform.auth.controller;

import chatapp.chat_platform.auth.dto.UpdateProfileRequest;
import chatapp.chat_platform.auth.service.ProfileService;
import chatapp.chat_platform.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(profileService.getProfile(userId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(ApiResponse.ok(profileService.getAllUsers(page)));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> updateProfile(@PathVariable UUID userId,
                                                         @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", profileService.updateProfile(userId, request)));
    }
}
