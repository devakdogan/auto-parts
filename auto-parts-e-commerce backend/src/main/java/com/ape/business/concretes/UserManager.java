package com.ape.business.concretes;

import com.ape.business.abstracts.ConfirmationTokenService;
import com.ape.business.abstracts.EmailService;
import com.ape.business.abstracts.RoleService;
import com.ape.business.abstracts.UserService;
import com.ape.entity.dao.ShoppingCartDao;
import com.ape.entity.dao.ShoppingCartItemDao;
import com.ape.entity.dao.UserDao;
import com.ape.entity.dto.UserDTO;
import com.ape.entity.dto.UserDeleteDTO;
import com.ape.entity.dto.request.LoginRequest;
import com.ape.entity.dto.request.PasswordUpdateRequest;
import com.ape.entity.dto.request.RegisterRequest;
import com.ape.entity.dto.request.UserUpdateRequest;
import com.ape.entity.dto.response.LoginResponse;
import com.ape.entity.dto.response.ResponseMessage;
import com.ape.entity.concrete.*;
import com.ape.entity.enums.RoleType;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.UserMapper;
import com.ape.security.SecurityUtils;
import com.ape.security.jwt.JwtUtils;
import com.ape.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final EntityManager entityManager;
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
    private final ShoppingCartItemDao shoppingCartItemDao;

    @Value("${management.autoparts.app.backendLink}")
    public String backendLink;
    @Value("${management.autoparts.app.resetPasswordLink}")
    public String frontendLink;
    @Value("${management.autoparts.app.maxFailedAttempts}")
    public int maxFailedAttempts;


    @Override
    public PageImpl<UserDTO> getAllUsersWithFilterAndPage(String query, RoleType role, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteriaQuery = cb.createQuery(UserEntity.class);
        Root<UserEntity> root = criteriaQuery.from(UserEntity.class);
        root.alias("generatedAlias0");
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<UserEntity> countRoot = countQuery.from(UserEntity.class);


        List<Predicate> predicates = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            String likeSearchText = "%" + query.toLowerCase(Locale.US) + "%";
            Predicate searchByUserFirstName = cb.like(cb.lower(root.get("firstName")), likeSearchText);
            Predicate searchByUserLastName = cb.like(cb.lower(root.get("lastName")), likeSearchText);
            Predicate searchByUserEmail = cb.like(cb.lower(root.get("email")), likeSearchText);
            predicates.add(cb.or(searchByUserFirstName,searchByUserLastName,searchByUserEmail));
        }
        if (role != null){
            Join<UserEntity, RoleEntity> joinRoles = root.join("roles");
            Join<UserEntity, RoleEntity> countRoles = countRoot.join("roles");
            joinRoles.alias("generatedAlias1");
            predicates.add(cb.equal(joinRoles.get("roleName"), role));
        }

        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));

        criteriaQuery.orderBy(pageable.getSort().stream()
                .map(order -> {
                    if (order.isAscending()) {
                        return cb.asc(root.get(order.getProperty()));
                    } else {
                        return cb.desc(root.get(order.getProperty()));
                    }
                })
                .collect(Collectors.toList()));

        criteriaQuery.select(root);
        criteriaQuery.where(finalPredicate);


        countQuery.select(cb.count(countRoot));
        countQuery.where(finalPredicate);
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();


        TypedQuery<UserEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());


        List<UserDTO> userDTOList = userMapper.entityListToDTOList(typedQuery.getResultList());

        return new PageImpl<>(userDTOList, pageable, totalRecords);
    }

    @Override
    @Transactional
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
        String link = frontendLink +"confirm?token="+token;
        emailService.send(
                registerRequest.getEmail(),
                emailManager.buildRegisterEmail(registerRequest.getFirstName(),link));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        UserEntity user = userDao.findByEmail(email);
        if (user==null){
            throw new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE);
        }
        return user;
    }

    @Override
    @Transactional
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
    public UserDTO getUserDTOById(Long id) {
        UserEntity user = userDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
        return userMapper.entityToDTO(user);
    }

    @Override
    public UserDTO getPrincipal() {
        UserEntity user=getCurrentUser();
        return userMapper.entityToDTO(user);
    }

    @Override
    public UserEntity getCurrentUser() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.PRINCIPAL_NOT_FOUND_MESSAGE));
        return getUserByEmail(email);
    }

    @Override
    public LoginResponse loginUser(String cartUUID, LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication  =  authenticationManager.
                authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetails  =  (UserDetails) authentication.getPrincipal() ;
        UserEntity user = getUserByEmail(userDetails.getUsername());
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

    @Override
    @Transactional
    public UserDTO updateUser(UserUpdateRequest userUpdateRequest) {
        UserEntity user = getCurrentUser();
        boolean emailExist = userDao.existsByEmail(userUpdateRequest.getEmail());
        if (emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, userUpdateRequest.getEmail()));
        }
        user.setEmail(userUpdateRequest.getEmail());
        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());
        user.setPhone(userUpdateRequest.getPhone());
        user.setBirthDate(userUpdateRequest.getBirthDate());
        user.setUpdateAt(LocalDateTime.now());
        userDao.save(user);
        return userMapper.entityToDTO(user);
    }



    @Override
    public long countUserRecords() {
       return userDao.count();
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return userDao.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, userId)));
    }

    @Override
    public UserDeleteDTO adminRemoveUserById(Long id) {
        UserEntity user = getUserById(id);
        user.setIsActive(false);
        user.setPassword("");
        Long shoppingCartId=user.getShoppingCart().getId();
        user.setShoppingCart(null);
        userDao.save(user);
        shoppingCartDao.deleteById(shoppingCartId);
        return userMapper.entityToUserDeleteDTO(user);
    }

    @Override
    public List<UserEntity> findUserByRole(RoleType role) {
        return userDao.findByRole(role);
    }

    @Override
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) {
        UserEntity user=getCurrentUser();
        if (!passwordEncoder.matches(passwordUpdateRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
        }

        String hashedPassword= passwordEncoder.encode(passwordUpdateRequest.getNewPassword());
        user.setPassword(hashedPassword);
        userDao.save(user);
    }
}
