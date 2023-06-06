package com.ape.business.concretes;

import com.ape.business.abstracts.ConfirmationTokenService;
import com.ape.business.abstracts.EmailService;
import com.ape.business.abstracts.RoleService;
import com.ape.business.abstracts.UserService;
import com.ape.dao.UserDao;
import com.ape.dto.request.RegisterRequest;
import com.ape.entity.ConfirmationToken;
import com.ape.entity.Role;
import com.ape.entity.User;
import com.ape.entity.enums.RoleType;
import com.ape.entity.enums.UserStatus;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserDao userDao;
    private final ConfirmationTokenService confirmationTokenService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailManager emailManager;
    @Value("${management.autoparts.app.backendLink}")
    private String backendLink;
    @Value("${management.autoparts.app.resetPasswordLink}")
    private String frontendLink;

    @Override
    public void createUser(RegisterRequest registerRequest) {
        Role role = roleService.findByRoleName(RoleType.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = null;
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        if (userDao.existsByEmail(registerRequest.getEmail())){
            user = getUserByEmail(registerRequest.getEmail());
            if (!user.getStatus().equals(UserStatus.ANONYMOUS)){
                throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,
                        registerRequest.getEmail()));
            }else{
                user.setFirstName(registerRequest.getFirstName());
                user.setLastName(registerRequest.getLastName());
                user.setEmail(registerRequest.getEmail());
                user.setBirthDate(registerRequest.getBirthDate());
                user.setPhone(registerRequest.getPhone());
                user.setStatus(UserStatus.PENDING);
                user.setPassword(encodedPassword);
                userDao.save(user);
            }
        }else {
            user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setBirthDate(registerRequest.getBirthDate());
            user.setPassword(encodedPassword);
            user.setPhone(registerRequest.getPhone());
            user.setRoles(roles);
            user.setStatus(UserStatus.PENDING);
            userDao.save(user);
        }
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusDays(1),user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = frontendLink+"confirm?token="+token;
        emailService.send(
                registerRequest.getEmail(),
                emailManager.buildRegisterEmail(registerRequest.getFirstName(),link));
    }


    @Override
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE));
    }
}
