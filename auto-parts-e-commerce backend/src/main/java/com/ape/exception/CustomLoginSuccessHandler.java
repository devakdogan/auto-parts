package com.ape.exception;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ape.business.concretes.UserManager;
import com.ape.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserManager userManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails =  (UserDetails) authentication.getPrincipal();
        UserEntity user = userManager.getUserByEmail(userDetails.getUsername());
        if (user.getLoginFailCount() > 0) {
            userManager.resetFailedAttempts(user);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
