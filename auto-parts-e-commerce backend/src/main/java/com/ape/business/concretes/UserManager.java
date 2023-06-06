package com.ape.business.concretes;

import com.ape.business.abstracts.ConfirmationTokenService;
import com.ape.business.abstracts.EmailService;
import com.ape.business.abstracts.RoleService;
import com.ape.business.abstracts.UserService;
import com.ape.dao.ShoppingCartDao;
import com.ape.dao.UserDao;
import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.UserDTO;
import com.ape.entity.ConfirmationToken;
import com.ape.entity.Role;
import com.ape.entity.ShoppingCart;
import com.ape.entity.User;
import com.ape.entity.enums.RoleType;
import com.ape.entity.enums.UserStatus;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
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
    private final ModelMapper modelMapper;
    @Value("${management.autoparts.app.backendLink}")
    private String backendLink;
    @Value("${management.autoparts.app.resetPasswordLink}")
    private String frontendLink;
    private final ShoppingCartDao shoppingCartDao;

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

    @Override
    public UserDTO confirmAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(ErrorMessage.EMAIL_ALREADY_CONFIRMED_MESSAGE);
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(ErrorMessage.TOKEN_EXPIRED_MESSAGE);
        }
        confirmationTokenService.setConfirmedAt(token);
        User user = getUserByEmail(confirmationToken.getUser().getEmail());
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartUUID(UUID.randomUUID().toString());
        shoppingCartDao.save(shoppingCart);
        user.setShoppingCart(shoppingCart);
        activateUser(user.getEmail());
        return entityToDto(user);
    }

    @Override
    public void activateUser(String email) {
        UserStatus status = UserStatus.ACTIVE;
        userDao.enableUser(status, email);
    }

    @Override
    public User dtoToEntity(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO entityToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
        return entityToDto(user);
    }
}
