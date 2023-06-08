package com.ape.business.concretes;

import com.ape.business.abstracts.ConfirmationTokenService;
import com.ape.business.abstracts.EmailService;
import com.ape.business.abstracts.RoleService;
import com.ape.business.abstracts.UserService;
import com.ape.dao.ShoppingCartDao;
import com.ape.dao.ShoppingCartItemDao;
import com.ape.dao.UserDao;
import com.ape.dto.UserDTO;
import com.ape.dto.request.LoginRequest;
import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.LoginResponse;
import com.ape.dto.response.ResponseMessage;
import com.ape.entity.*;
import com.ape.entity.enums.RoleType;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.UserMapper;
import com.ape.security.jwt.JwtUtils;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserDao userDao;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final EmailManager emailManager;
    private final UserMapper userMapper;
    private final ShoppingCartDao shoppingCartDao;


    @Value("${management.autoparts.app.backendLink}")
    private String backendLink;
    @Value("${management.autoparts.app.resetPasswordLink}")
    private String frontendLink;
    private final ShoppingCartItemDao shoppingCartItemDao;


    @Override
    public void createUser(RegisterRequest registerRequest) {
        RoleEntity roleEntity = roleService.findByRoleName(RoleType.ROLE_USER);
        Set<RoleEntity> roleList = new HashSet<>();
        roleList.add(roleEntity);

        UserEntity user = null;
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        if (userDao.existsByEmail(registerRequest.getEmail())){
            user = getUserByEmail(registerRequest.getEmail());
            if (!user.getIsActive()){
                throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,
                        registerRequest.getEmail()));
            }else{
                user.setFirstName(registerRequest.getFirstName());
                user.setLastName(registerRequest.getLastName());
                user.setEmail(registerRequest.getEmail());
                user.setBirthDate(registerRequest.getBirthDate());
                user.setPhone(registerRequest.getPhone());
                user.setIsActive(false);
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
            user.setIsActive(false);
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
        UserEntity user = getUserByEmail(confirmationToken.getUser().getEmail());
        user.setIsActive(true);
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        shoppingCart.setCartUUID(UUID.randomUUID().toString());
        shoppingCartDao.save(shoppingCart);
        user.setShoppingCart(shoppingCart);
        userDao.save(user);
        return userMapper.entityToDTO(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        UserEntity user = userDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
        return userMapper.entityToDTO(user);
    }

    @Override
    public LoginResponse loginUser(String cartUUID, LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication  =  authenticationManager.
                authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetails  =  (UserDetails) authentication.getPrincipal() ;
        UserEntity user = getUserByEmail(userDetails.getUsername());
        boolean isAccountExists = userDao.existsByEmail(loginRequest.getEmail());
        if (!authentication.isAuthenticated() && isAccountExists){
            if (user.getLoginFailCount()<=5){
                user.setLoginFailCount(user.getLoginFailCount()+1);
            }
        throw new BadRequestException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE));
        }
        ShoppingCartEntity anonymousCart = shoppingCartDao.findByCartUUID(cartUUID).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,cartUUID)));
        ShoppingCartEntity userCart = user.getShoppingCart();
        if (anonymousCart.getShoppingCartItems().size()!=0){
            if (userCart.getShoppingCartItems().size()==0) {
                for (ShoppingCartItemEntity anonymousCartItem: anonymousCart.getShoppingCartItems()) {
                    ShoppingCartItemEntity shoppingCartItem = new ShoppingCartItemEntity();
                    shoppingCartItem.setProduct(anonymousCartItem.getProduct());
                    shoppingCartItem.setQuantity(anonymousCartItem.getQuantity());
                    shoppingCartItem.setTotalPrice(anonymousCartItem.getTotalPrice());
                    shoppingCartItem.setShoppingCart(userCart);
                    userCart.setGrandTotal(anonymousCart.getGrandTotal());
                    shoppingCartItemDao.save(shoppingCartItem);
                }
                shoppingCartDao.save(userCart);
            }else{
                for (ShoppingCartItemEntity anonymousCartItem: anonymousCart.getShoppingCartItems()) {
                    boolean merged = false;
                    for (ShoppingCartItemEntity userCartItem: userCart.getShoppingCartItems()) {
                        if (anonymousCartItem.getProduct().getId().longValue()==userCartItem.getProduct().getId().longValue()) {
                            if ((userCartItem.getQuantity()+anonymousCartItem.getQuantity())>userCartItem.getProduct().getStockAmount()){
                                int tempQuantity = userCartItem.getQuantity();
                                int stockAmount = userCartItem.getProduct().getStockAmount();
                                userCartItem.setQuantity(stockAmount);
                                userCartItem.setTotalPrice(stockAmount*userCartItem.getProduct().getPrice());
                                userCart.setGrandTotal(userCart.getGrandTotal()+((stockAmount-tempQuantity)*userCartItem.getProduct().getPrice()));
                            }else{
                                userCartItem.setQuantity(userCartItem.getQuantity() + anonymousCartItem.getQuantity());
                                userCartItem.setTotalPrice(userCartItem.getTotalPrice() + anonymousCartItem.getTotalPrice());
                                userCart.setGrandTotal(userCart.getGrandTotal()+anonymousCartItem.getTotalPrice());
                            }
                            merged = true;
                            break;
                        }
                    }
                    if (!merged){
                        ShoppingCartItemEntity unmergedItem = new ShoppingCartItemEntity();
                        unmergedItem.setQuantity(anonymousCartItem.getQuantity());
                        unmergedItem.setProduct(anonymousCartItem.getProduct());
                        unmergedItem.setTotalPrice(anonymousCartItem.getTotalPrice());
                        unmergedItem.setShoppingCart(userCart);
                        userCart.setGrandTotal(userCart.getGrandTotal()+unmergedItem.getTotalPrice());
                        shoppingCartItemDao.save(unmergedItem);
                    }
                }
            }
        }
        shoppingCartDao.save(userCart);
        shoppingCartDao.delete(anonymousCart);


        if (!user.getIsActive()){
            throw new BadRequestException(String.format(ErrorMessage.EMAIL_NOT_CONFIRMED_MESSAGE,user.getEmail()));
        }
        String jwtToken =   jwtUtils.generateJwtToken(userDetails);
        String userCartUUID = user.getShoppingCart().getCartUUID();
        return new LoginResponse(ResponseMessage.LOGIN_SUCCESS,true, jwtToken, userCartUUID);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userDao.findAll();
        return userMapper.entityListToDTOList(users);
    }
}
