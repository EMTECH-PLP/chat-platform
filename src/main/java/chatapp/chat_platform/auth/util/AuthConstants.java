package chatapp.chat_platform.auth.util;

public final class AuthConstants {
    
    private AuthConstants() {}
    
    // Validation
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int TOKEN_EXPIRY_HOURS = 24;
    
    // JWT
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_ID_ATTRIBUTE = "userId";
    public static final String USERNAME_ATTRIBUTE = "username";
    public static final String EMAIL_ATTRIBUTE = "email";
    public static final String ROLE_ATTRIBUTE = "role";
    
    // Security
    public static final int BCRYPT_STRENGTH = 12;
    
    // Messages
    public static final String MSG_USERNAME_TAKEN = "Username is already taken";
    public static final String MSG_EMAIL_REGISTERED = "Email is already registered";
    public static final String MSG_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String MSG_EMAIL_NOT_VERIFIED = "Please verify your email before logging in";
    public static final String MSG_ACCOUNT_NOT_ACTIVATED = "Account is not activated. Please verify your email first.";
    public static final String MSG_INVALID_TOKEN = "Invalid or expired token";
    public static final String MSG_EMAIL_VERIFIED = "Email verified successfully";
    public static final String MSG_REGISTRATION_SUCCESS = "User registered successfully. Please check your email for verification.";
    public static final String MSG_LOGIN_SUCCESS = "Login successful";
}