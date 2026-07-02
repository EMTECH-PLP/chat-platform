package chatapp.chat_platform.auth.config;

import chatapp.chat_platform.auth.util.AuthConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Order(1)
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        logger.debug("🔍 Filter checking path: {}", path);

        // Skip filter for public endpoints
        if (shouldNotFilter(path)) {
            logger.debug("✅ Public endpoint - skipping auth: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractRole(token);

                    request.setAttribute(AuthConstants.USER_ID_ATTRIBUTE, jwtUtil.extractUserId(token));
                    request.setAttribute(AuthConstants.USERNAME_ATTRIBUTE, username);
                    request.setAttribute(AuthConstants.EMAIL_ATTRIBUTE, jwtUtil.extractEmail(token));
                    request.setAttribute(AuthConstants.ROLE_ATTRIBUTE, role);
                    logger.debug("✅ Token validated for user: {}", username);

                    // Bridge the gap to populate Spring Security's Principal context
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } else {
                    logger.warn("❌ Invalid or expired token");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"success\":false,\"message\":\"Invalid or expired token\",\"data\":null}");
                    return;
                }
            } catch (Exception e) {
                logger.error("❌ Error validating token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Invalid token\",\"data\":null}");
                return;
            }
        } else {
            logger.warn("❌ No Authorization header for: {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Authentication required\",\"data\":null}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldNotFilter(String path) {
        // AUTH public endpoints
        if (path.equals("/api/auth/register") ||
                path.equals("/api/auth/login") ||
                path.equals("/api/auth/health") ||
                path.equals("/api/auth/verify-email") ||
                path.equals("/api/auth/resend-verification") ||
                path.startsWith("/api/auth/email-verified") ||
                path.equals("/api/auth/password/forgot") ||
                path.equals("/api/auth/password/reset")) {

            return true;
        }

        // SWAGGER public endpoints
        if (path.equals("/api/docs") ||
                path.startsWith("/api/docs/") ||
                path.startsWith("/api/swagger-ui") ||
                path.startsWith("/api/v3/api-docs") ||
                path.startsWith("/api/swagger-resources") ||
                path.startsWith("/api/webjars")) {
            return true;
        }

        return false;
    }
}