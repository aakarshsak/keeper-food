package com.foodkeeper.config;

import com.foodkeeper.model.AuthProvider;
import com.foodkeeper.model.User;
import com.foodkeeper.repository.UserRepository;
import com.foodkeeper.security.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        
        User user = processOAuth2User(registrationId, oAuth2User);
        String token = jwtUtils.generateTokenFromUsername(user.getEmail());
        
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("token", token)
                .queryParam("error", false)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User processOAuth2User(String registrationId, OAuth2User oAuth2User) {
        if ("google".equals(registrationId)) {
            return processGoogleUser(oAuth2User);
        }
        throw new RuntimeException("Unsupported OAuth2 provider: " + registrationId);
    }

    private User processGoogleUser(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
        String googleId = (String) attributes.get("sub");
        
        // Split name into first and last name
        String[] nameParts = name.split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        return userRepository.findByEmail(email)
                .map(user -> updateExistingUser(user, firstName, lastName, picture))
                .orElseGet(() -> createNewUser(email, firstName, lastName, picture, googleId));
    }

    private User updateExistingUser(User existingUser, String firstName, String lastName, String picture) {
        boolean updated = false;
        
        if (!firstName.equals(existingUser.getFirstName())) {
            existingUser.setFirstName(firstName);
            updated = true;
        }
        
        if (!lastName.equals(existingUser.getLastName())) {
            existingUser.setLastName(lastName);
            updated = true;
        }
        
        if (picture != null && !picture.equals(existingUser.getProfilePicture())) {
            existingUser.setProfilePicture(picture);
            updated = true;
        }
        
        if (existingUser.getProvider() == AuthProvider.LOCAL) {
            existingUser.setProvider(AuthProvider.GOOGLE);
            updated = true;
        }
        
        if (!existingUser.isEmailVerified()) {
            existingUser.setEmailVerified(true);
            updated = true;
        }
        
        if (updated) {
            return userRepository.save(existingUser);
        }
        
        return existingUser;
    }

    private User createNewUser(String email, String firstName, String lastName, String picture, String googleId) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setProfilePicture(picture);
        newUser.setProvider(AuthProvider.GOOGLE);
        newUser.setProviderId(googleId);
        newUser.setEmailVerified(true); // Google emails are pre-verified
        newUser.setEnabled(true);
        
        return userRepository.save(newUser);
    }
} 