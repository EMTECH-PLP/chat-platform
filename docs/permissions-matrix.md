# Permissions Matrix

Base path: `/api`

Current security model:
- `SecurityConfig` permits every request at the Spring Security layer.
- `JwtAuthFilter` blocks any non-public request unless a valid Bearer token is present.
- The token carries a `role` claim, but the application does not currently enforce role-based rules or ownership checks.

## Current Effective Access

| Area | Endpoint(s) | Current access | Notes |
|---|---|---|---|
| Authentication | `POST /api/auth/register` | Public | User registration. |
| Authentication | `POST /api/auth/login` | Public | Returns JWT tokens after verification checks. |
| Authentication | `GET /api/auth/health` | Public | Service health check. |
| Verification | `POST /api/auth/verify-email` | Public | Uses verification token from email. |
| Verification | `POST /api/auth/resend-verification` | Public | Resends verification email by email address. |
| Verification | `GET /api/auth/email-verified/{userId}` | Public | Reads verification status by user ID. |
| Password recovery | `POST /api/auth/password/forgot` | Public | Generates reset token. |
| Password recovery | `POST /api/auth/password/reset` | Public | Resets password with token. |
| Profile | `GET /api/profiles/{userId}` | Authenticated user | No ownership check. Any valid token can read any profile. |
| Profile | `GET /api/profiles` | Authenticated user | Lists users. No role restriction. |
| Profile | `PATCH /api/profiles/{userId}` | Authenticated user | No ownership check. Any valid token can update any profile ID. |
| Chat | `POST /api/rooms` | Authenticated user | Room creation accepts `createdByUserId` in the payload. |
| Chat | `POST /api/rooms/{roomId}/join/{userId}` | Authenticated user | Any authenticated caller can add any user ID to a room. |
| Chat | `POST /api/rooms/{roomId}/messages` | Authenticated user | Message sender is provided in the payload. |
| Chat | `GET /api/rooms/{roomId}/messages` | Authenticated user | No room membership check. |
| Chat | `GET /api/rooms/user/{userId}` | Authenticated user | Returns rooms for any user ID. |
| Notifications | `POST /api/notifications` | Authenticated user | Notification creation is not restricted. |
| Notifications | `GET /api/notifications/user/{userId}` | Authenticated user | No ownership check. |
| Notifications | `GET /api/notifications/user/{userId}/unread` | Authenticated user | No ownership check. |
| Notifications | `GET /api/notifications/user/{userId}/unreadcount` | Authenticated user | No ownership check. |
| Notifications | `PATCH /api/notifications/{id}/read` | Authenticated user | Marks any notification ID as read. |
| Notifications | `PUT /api/notifications/user/{userId}/read-all` | Authenticated user | No ownership check. |
| Search | `POST /api/search` | Authenticated user | Search is unrestricted once authenticated. |
| Docs | `/api/docs`, `/api/swagger-ui/**`, `/api/v3/api-docs/**` | Public | Swagger and API docs remain open. |

## Suggested Target-State Policy

If you want the app to behave like a conventional permissions system, these are the likely rules:

| Role / actor | Allowed capabilities |
|---|---|
| Anonymous | Register, login, verify email, resend verification, forgot password, reset password, health check, docs. |
| Authenticated user | Read own profile, update own profile, create/join rooms they are allowed into, send messages to rooms they belong to, read own notifications, mark own notifications as read, search rooms they belong to. |
| Admin | View all profiles, manage user access, read/modify any room or notification record, audit search/index data. |
| Service/internal | Create notifications and index/search messages on behalf of the app. |

## Gaps In The Current Code

- No `@PreAuthorize` or equivalent method security is present.
- `role` is carried in the JWT, but no controller or service checks it.
- Several endpoints accept `userId` or `senderId` from the client body/path instead of deriving identity from the authenticated principal.
- Room, profile, and notification operations do not verify ownership or membership.

