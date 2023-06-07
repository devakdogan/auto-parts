package com.ape.business.concretes;

import com.ape.business.abstracts.ConfirmationTokenService;
import com.ape.business.abstracts.EmailService;
import com.ape.business.abstracts.RoleService;
import com.ape.business.abstracts.UserService;
import com.ape.dao.ShoppingCartDao;
import com.ape.dao.UserDao;
import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.UserDTO;
import com.ape.entity.ConfirmationTokenEntity;
import com.ape.entity.RoleEntity;
import com.ape.entity.ShoppingCartEntity;
import com.ape.entity.UserEntity;
import com.ape.entity.enums.RoleType;
import com.ape.entity.enums.UserStatus;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final ShoppingCartDao shoppingCartDao;


    @Value("${management.autoparts.app.backendLink}")
    private String backendLink;
    @Value("${management.autoparts.app.resetPasswordLink}")
    private String frontendLink;


    @Override
    public void createUser(RegisterRequest registerRequest) {
        RoleEntity roleEntity = roleService.findByRoleName(RoleType.USER);
        Set<RoleEntity> roleList = new HashSet<>();
        roleList.add(roleEntity);

        UserEntity user = null;
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
            user = new UserEntity();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setBirthDate(registerRequest.getBirthDate());
            user.setPassword(encodedPassword);
            user.setPhone(registerRequest.getPhone());
            user.setRoles(roleList);
            user.setStatus(UserStatus.PENDING);
            userDao.save(user);
        }
        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationTokenEntity = new ConfirmationTokenEntity(token, LocalDateTime.now(),LocalDateTime.now().plusDays(1), user);
        confirmationTokenService.saveConfirmationToken(confirmationTokenEntity);
        String link = frontendLink+"confirm?token="+token;
        emailService.send(
                registerRequest.getEmail(),
                emailManager.buildRegisterEmail(registerRequest.getFirstName(),link));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public UserDTO confirmAccount(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(ErrorMessage.EMAIL_ALREADY_CONFIRMED_MESSAGE);
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(ErrorMessage.TOKEN_EXPIRED_MESSAGE);
        }
        confirmationTokenService.setConfirmedAt(token);
        UserEntity user = getUserByEmail(confirmationToken.getUserEntity().getEmail());
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartEntity.setCartUUID(UUID.randomUUID().toString());
        shoppingCartDao.save(shoppingCartEntity);
        user.setShoppingCartEntity(shoppingCartEntity);
        activateUser(user.getEmail());
        return userMapper.entityToDTO(user);
    }

    @Override
    public void activateUser(String email) {
        UserStatus status = UserStatus.ACTIVE;
        userDao.enableUser(status, email);
    }

    @Override
    public UserDTO getUserById(Long id) {
        UserEntity user = userDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
        return userMapper.entityToDTO(user);
    }
}
