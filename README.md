# Chat Platform - Multi-API Backend System

A real-time chat platform backend built with Spring Boot, featuring JWT authentication, email verification, and modular API design.

---

## 📋 Project Overview

This is a complete backend system for a real-time chat platform, built as a collaborative project with teams working on separate API modules. The system includes user authentication, profile management, chat rooms, notifications, and search functionality.

### Modules

| Module | Team | Description | Status |
|--------|------|-------------|--------|
| **Auth Module** | Dev A | User registration, login, JWT tokens, email verification | ✅ Complete |
| **Profile Module** | Dev B | User profile management (GET/PATCH/DELETE /me) | ✅ Complete |
| **Password Module** | Dev C | Forgot password, reset password, change password | ⏳ In Progress |
| **Chat Module** | Dev D | Room creation, messaging, room membership | ⏳ In Progress |
| **Notification Module** | Dev E | User notifications for chat activity | ⏳ In Progress |
| **Search Module** | Dev F | Message history search and indexing | ⏳ In Progress |

---

## 🚀 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21 | Core language |
| **Spring Boot** | 4.0.2 | Application framework |
| **Spring Security** | 4.0.2 | Authentication & authorization |
| **Spring Data JPA** | 4.0.2 | Database ORM |
| **MariaDB** | 11.8.6 | Primary database |
| **PostgreSQL** | - | Alternative database (team choice) |
| **JWT (JJWT)** | 0.12.5 | Token generation & validation |
| **Lombok** | 1.18.36 | Boilerplate reduction |
| **Swagger/OpenAPI** | 2.7.0 | API documentation |
| **Resend** | 2.0.0 | Email service (production) |
| **SMTP (Gmail)** | - | Email service (testing without domain verification) |
| **Maven** | 3.9.x | Build tool |

---

## 📁 Project Structure

```
chat-platform/
├── src/main/java/chatapp/chat_platform/
│   ├── auth/                           # Auth Module (COMPLETE)
│   │   ├── config/                     # Security, JWT, Swagger configs
│   │   │   ├── SecurityConfig.java     # Spring Security configuration
│   │   │   ├── JwtUtil.java            # JWT token utilities
│   │   │   ├── JwtAuthFilter.java      # JWT authentication filter
│   │   │   └── SwaggerConfig.java      # API documentation config
│   │   ├── controller/                 # REST endpoints
│   │   │   ├── AuthController.java     # Register & login endpoints
│   │   │   └── VerificationController.java # Email verification endpoints
│   │   ├── dto/                        # Data Transfer Objects
│   │   │   ├── request/                # Request DTOs
│   │   │   └── response/               # Response DTOs
│   │   ├── exception/                  # Global exception handling
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── model/                      # JPA entities
│   │   │   ├── User.java               # User entity
│   │   │   ├── VerificationToken.java  # Email verification tokens
│   │   │   └── PasswordReset.java      # Password reset tokens
│   │   ├── repository/                 # Data access layer
│   │   │   ├── UserRepository.java
│   │   │   ├── VerificationTokenRepository.java
│   │   │   └── PasswordResetRepository.java
│   │   ├── service/                    # Business logic
│   │   │   ├── AuthService.java        # Authentication logic
│   │   │   ├── VerificationService.java # Email verification logic
│   │   │   ├── EmailService.java       # Email interface
│   │   │   ├── ConsoleEmailService.java # Console fallback
│   │   │   ├── ResendEmailService.java # Resend email provider
│   │   │   └── GmailSmtpEmailService.java # Gmail SMTP provider
│   │   └── util/                       # Constants & utilities
│   │       └── AuthConstants.java
│   ├── chat/                           # Chat Module (WIP)
│   ├── notification/                   # Notification Module (WIP)
│   ├── search/                         # Search Module (WIP)
│   └── common/                         # Shared components
│       └── response/
│           └── ApiResponse.java        # Standardized API responses
├── src/main/resources/
│   ├── application.yml                 # Main configuration
│   ├── application-mariadb.yml         # MariaDB configuration (gitignored)
│   └── application-mariadb-sample.yml  # Template for team
├── scripts/
│   └── test-auth.sh                    # Auth module test script
├── .env                                # Environment variables (gitignored)
├── .env.example                        # Environment variables template
├── pom.xml                             # Maven dependencies
└── README.md                           # This file
```

---

## 🔐 Auth Module Features

### Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/auth/register` | Register a new user | ❌ No |
| `POST` | `/api/auth/login` | Login and receive JWT tokens | ❌ No |
| `POST` | `/api/auth/verify-email` | Verify email with token | ❌ No |
| `POST` | `/api/auth/resend-verification` | Resend verification email | ❌ No |
| `GET` | `/api/auth/health` | Health check | ❌ No |
| `GET` | `/api/auth/email-verified/{userId}` | Check email verification status | ✅ Yes (JWT) |

### Security Features

| Feature | Implementation |
|---------|----------------|
| **Password Hashing** | BCrypt (strength 12) |
| **JWT Tokens** | HS256 with Access + Refresh tokens |
| **Token Expiry** | Access: 15 min, Refresh: 7 days |
| **Email Verification** | 24-hour token expiry |
| **Account Lock** | Disabled until email verified |
| **Soft Delete** | Users can be soft-deleted |
| **CORS** | Configured for frontend integration |

### Authentication Flow

```
1. User Registers → 2. Verification Email Sent → 3. User Verifies Email → 4. User Logs In → 5. JWT Tokens Issued → 6. Access Protected Endpoints
```

---

## 📧 Email Configuration

### Supported Email Providers

| Provider | Use Case | Domain Verification Required |
|----------|----------|------------------------------|
| **Console** | Development (logs token to console) ||
| **Gmail SMTP** | Testing (real email delivery) ||


## 🛠️ Setup Instructions

### Prerequisites

- **Java 21** or higher
- **MariaDB** or **PostgreSQL** installed and running
- **Maven** installed

### Step 1: Clone the Repository

```bash
git clone https://github.com/EMTECH-PLP/chat-platform.git
cd chat-platform
```

### Step 2: Configure Database

Copy the sample configuration:

```bash
cp src/main/resources/application-mariadb-sample.yml src/main/resources/application-mariadb.yml
```

Edit `application-mariadb.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/chat_db
    username: root
    password: your_password_here
```

Or use environment variables:

```bash
export DB_PASSWORD=your_password
```

### Step 3: Create Database

```bash
# Login to MariaDB
mysql -u root -p

# Create database
CREATE DATABASE chat_db;
EXIT;
```

### Step 4: Set Environment Variables

Create `.env` file:

```bash
cp .env.example .env
```

Edit `.env` with your values:

```bash
EMAIL_PROVIDER=console
FRONTEND_URL=http://localhost:3000
```

### Step 5: Build and Run

```bash
# Clean and build
mvn clean compile -Dmaven.test.skip=true

# Run the application
mvn spring-boot:run
```

### Step 6: Test the API

```bash
# Health check
curl http://localhost:8080/api/auth/health

# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","email":"demo@example.com","password":"SecurePass123!"}'

# Check logs for verification token
# Look for: 🔗 VERIFICATION LINK: http://localhost:3000/verify-email?token=xxxxx

# Verify email
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{"token":"YOUR_TOKEN"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"SecurePass123!"}'
```

### Step 7: Swagger API Docs

Open in browser:
```
http://localhost:8080/api/docs
```

---

## 🧪 Testing

### Automated Test Script

```bash
# Make executable
chmod +x scripts/test-auth.sh

# Run the test
./scripts/test-auth.sh
```

### Manual Testing Commands

```bash
# 1. Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"SecurePass123!"}'

# 2. Check logs for verification token

# 3. Verify email (replace YOUR_TOKEN)
curl -X POST http://localhost:8080/api/auth/verify-email \
  -H "Content-Type: application/json" \
  -d '{"token":"YOUR_TOKEN"}'

# 4. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"SecurePass123!"}'

# 5. Access protected endpoint (replace YOUR_ACCESS_TOKEN)
curl -X GET http://localhost:8080/api/auth/health \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```
---

## 🔒 Environment Variables

| Variable | Description | Required For |
|----------|-------------|--------------|
| `EMAIL_PROVIDER` | Email provider (console/resend/gmail) | All |
| `GMAIL_USERNAME` | Gmail address | Gmail SMTP |
| `GMAIL_APP_PASSWORD` | Gmail App Password | Gmail SMTP |
| `FRONTEND_URL` | Frontend application URL | All |
| `DB_PASSWORD` | Database password | Optional |
| `JWT_SECRET` | JWT secret key (32+ chars) | Optional |

---

## 🤝 Team Collaboration

### Branch Strategy

| Developer | Branch | Module |
|-----------|--------|--------|
| Dev A (You) | `bkioko-authcontrol` | Auth Module  |
| Dev B | `obunde-profile-man` | Profile Module  |
| Dev C | `feature/password` | Password Module |
| Dev D | `feature/chat-module` | Chat Module |
| Dev E | `feature/notification` | Notification Module |
| Dev F | `feature/search` | Search Module |

### Merging to Main

```bash
# 1. Ensure your branch is up to date
git checkout your-branch
git pull origin main

# 2. Switch to main
git checkout main
git pull origin main

# 3. Merge your branch
git merge your-branch

# 4. Resolve conflicts (if any)
# 5. Push main
git push origin main
```

---

## ⚠️ Common Issues & Fixes

### Lombok Not Working

**Solution:** Enable annotation processing in IntelliJ:
- `File` → `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
- ✅ Check **Enable annotation processing**

# JWT Secret Must Be 32+ Characters

**Solution:** Generate a secure secret:
```bash
openssl rand -base64 32
```
Update `jwt.secret` in `application.yml` or set `JWT_SECRET` in `.env`.

---

## 🚀 Deployment

### Building JAR File

```bash
mvn clean package -Dmaven.test.skip=true
```

### Running JAR

```bash
java -jar target/chat-platform-0.0.1-SNAPSHOT.jar
```

### With Environment Variables

```bash
EMAIL_PROVIDER=resend \
RESEND_API_KEY=re_xxxxx \
RESEND_FROM_EMAIL=noreply@yourdomain.com \
java -jar target/chat-platform-0.0.1-SNAPSHOT.jar
```

---

## 📊 Response Format

All API responses follow a consistent format:

### Success Response

```json
{
  "Success": true,
  "Message": "Operation successful",
  "Data": {
    // Response data
  },
  "Timestamp": "2026-07-03T00:00:00.000000000"
}
```

### Error Response

```json
{
  "Success": false,
  "Message": "Error description",
  "Data": null,
  "Timestamp": "2026-07-03T00:00:00.000000000"
}
```

---

## 📝 API Documentation

Swagger UI is available at:
```
http://localhost:8080/api/docs
```

The OpenAPI JSON specification is available at:
```
http://localhost:8080/api/v3/api-docs
```

---

## 📈 Future Improvements

- [ ] Add rate limiting (prevent brute force)
- [ ] Add account lockout after failed attempts
- [ ] Add two-factor authentication (2FA)
- [ ] Add social login (Google, GitHub)
- [ ] Add user activity logging
- [ ] Implement refresh token rotation
- [ ] Add email templates with HTML formatting
- [ ] Add email sending with attachments

---

## 📄 License

This project is for educational purposes.

---

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- Team members for collaborative development
- Gmail for SMTP service

---

**Built with ❤️ by Team Chat-Platform**