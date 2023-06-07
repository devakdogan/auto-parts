package com.ape.security.service;
import com.ape.business.concretes.UserManager;
import com.ape.entity.UserEntity;
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
        UserEntity userEntity =  userManager.getUserByEmail(email);
        return UserDetailsImpl.build(userEntity);
    }
}
