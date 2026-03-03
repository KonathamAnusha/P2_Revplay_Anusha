package com.rev.config;

import com.rev.entity.UserAccount;
import com.rev.service.UserServiceInterface;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private final UserServiceInterface userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        log.info("Login success for: {}", email);

        // Load full user details
        UserAccount user = userService.getUserByEmail(email);

        // Put into session exactly as expected by DashboardController
        HttpSession session = request.getSession();
        session.setAttribute("loggedUser", user);

        log.info("User role: {}", user.getRole());

        // Safely try to load artist profile (may be lazy-loaded)
        if (user.getRole() == UserAccount.Role.ARTIST) {
            try {
                if (user.getArtistProfile() != null) {
                    session.setAttribute("loggedArtist", user.getArtistProfile());
                }
            } catch (Exception e) {
                log.warn("Could not load artist profile for user {}: {}", email, e.getMessage());
            }
        }

        // Redirect to dashboard
        response.sendRedirect("/dashboard");
    }
}
