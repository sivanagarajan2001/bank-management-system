package com.basesetup.login.config;

import com.basesetup.login.dto.UserContextDto;
import com.basesetup.login.model.User;
import com.basesetup.login.repository.UserRepository;
import com.basesetup.login.service.JwtService;
import com.basesetup.login.service.RedisSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RedisSessionService sessionService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail != null && authentication == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                //  DEBUG (ADD THIS LINE)
                System.out.println("Authorities from UserDetails: " + userDetails.getAuthorities());
                //  Extract sessionId from JWT

                String sessionId = jwtService.extractClaim(jwt, claims -> claims.get("sessionId", String.class));

                //  Validate session from Redis
                if (!sessionService.validateSession(userEmail, sessionId)) {
                    throw new RuntimeException("Session expired or invalid");
                }

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    loadUserContextHolder(userEmail);
                }
            }


            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        } finally {
            UserContextHolder.clear();
        }
    }


    private void loadUserContextHolder(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            UserContextDto userContext = new UserContextDto();
            userContext.setId(user.get().getId());
            userContext.setEmail(user.get().getEmail());
            userContext.setUsername(user.get().getUsername());
            userContext.setFirstName(user.get().getFirstName());
            userContext.setLastName(user.get().getLastName());
            UserContextHolder.setUserContext(userContext);
        }
    }
}