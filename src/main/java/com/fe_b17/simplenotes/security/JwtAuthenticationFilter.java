/*  src/main/java/com/fe_b17/simplenotes/security/JwtAuthenticationFilter.java  */
package com.fe_b17.simplenotes.security;

import com.fe_b17.simplenotes.ZoneContext;
import com.fe_b17.simplenotes.session.service.SessionService;
import com.fe_b17.simplenotes.user.models.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SessionService sessionService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {
            String auth = req.getHeader("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) {
                chain.doFilter(req, res);
                return;
            }

            String token = auth.substring(7);
            jwtService.validateToken(token);

            UUID sessionId = jwtService.extractSessionId(token);
            if (!sessionService.isActive(sessionId)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Session expired or revoked");
                return;
            }

            String email = jwtService.extractUserEmail(token);
            UserPrincipal principal = (UserPrincipal)
                    userDetailsService.loadUserByUsername(email);

            var authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    principal.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(req));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String zoneClaim = jwtService.extractClaim(token,
                    claims -> claims.get("zone").toString());
            ZoneContext.set(ZoneId.of(zoneClaim));

            chain.doFilter(req, res);

        } finally {
            ZoneContext.clear();
        }
    }
}
