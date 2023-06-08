package com.ape.exception;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ape.business.concretes.UserManager;
import com.ape.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component

public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserManager userManager;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        UserEntity user = userManager.getUserByEmail(email);

        if (user != null) {
            if (user.getIsActive() && !user.getIsLocked()) {
                if (user.getLoginFailCount() < userManager.maxFailedAttempts - 1) {
                    userManager.increaseFailedAttempts(user);
                } else {
                    userManager.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts.");
                }
            }

            super.setDefaultFailureUrl("/login?error");
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}