package com.ape.security.service;
import com.ape.business.abstracts.UserService;
import com.ape.entity.User;
import com.gpm.domains.User;
import com.gpm.exception.ResourceNotFoundException;
import com.gpm.exception.message.ErrorMessage;
import com.gpm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {

        User user =  userService.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE));
        return UserDetailsImpl.build(user);
    }
}
