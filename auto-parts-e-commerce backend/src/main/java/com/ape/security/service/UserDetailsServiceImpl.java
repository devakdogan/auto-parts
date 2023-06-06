package com.ape.security.service;
import com.ape.business.abstracts.UserService;
import com.ape.business.concretes.UserManager;
import com.ape.dao.UserDao;
import com.ape.entity.User;
import com.ape.exception.ResourceNotFoundException;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserManager userManager;

    @Override
    public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {
        User user =  userManager.getUserByEmail(email);
        return UserDetailsImpl.build(user);
    }
}
