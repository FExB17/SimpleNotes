package com.fe_b17.simplenotes.security;

import com.fe_b17.simplenotes.ZoneContext;
import com.fe_b17.simplenotes.session.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    protected void doFilterInternal
            ( HttpServletRequest request,
              HttpServletResponse response,
              FilterChain filterChain
            ) throws ServletException, IOException {

        try{
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("no valid authorization header/ might be logging in");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            jwtService.validateToken(token);

            UUID sessionId = jwtService.extractSessionId(token);
            if (!sessionService.isActive(sessionId)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid session or expired");
                return;
            }

            String email = jwtService.extractUserEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
            System.out.println("User added to SecurityContext: " + email);
            String zoneIdStr = jwtService.extractClaim(token, claims -> claims.get("zone").toString());
            ZoneContext.set(ZoneId.of(zoneIdStr));
            System.out.println("Token: " + token);

        }finally{
            ZoneContext.clear();
        }
    }
}
