package chatapp.chat_platform.auth.controller;

import chatapp.chat_platform.auth.dto.request.AdminUserRequest;
import chatapp.chat_platform.auth.service.AdminService;
import chatapp.chat_platform.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin-only user management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Delete a user", description = "Soft-deletes a user by ID. Admin only.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable UUID userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.ok("User deleted successfully", null));
    }

    @Operation(summary = "Update a user", description = "Update any user's profile or password. Admin only.")
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable UUID userId,
                                                      @Valid @RequestBody AdminUserRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User updated successfully", adminService.updateUser(userId, request)));
    }
}
