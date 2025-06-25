package com.fe_b17.simplenotes.security;

import com.fe_b17.simplenotes.ZoneContext;
import com.fe_b17.simplenotes.service.JwtService;
import com.fe_b17.simplenotes.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    protected void doFilterInternal
            ( HttpServletRequest request,
              HttpServletResponse response,
              FilterChain filterChain
            ) throws ServletException, IOException {

        try{
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("no valid authorization header");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            if (!jwtService.isValidToken(token)) {
                System.out.println("invalid Token");
                filterChain.doFilter(request, response);
                return;
            }

            UUID sessionId = jwtService.extractSessionId(token);
            if (!sessionService.isActive(sessionId)) {
                System.out.println("invalid Session or not existent");
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtService.extractUserEmail(token);


            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    null
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("User added to SecurityContext: " + email);
            String zoneIdStr = jwtService.extractClaim(token, claims -> claims.get("zone").toString());
            ZoneContext.set(ZoneId.of(zoneIdStr));

            System.out.println("Token: " + token);
            filterChain.doFilter(request, response);
        }finally{
            ZoneContext.clear();
        }
    }
}
