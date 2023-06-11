package com.ape.security.service;
import com.ape.entity.dao.UserDao;
import com.ape.entity.concrete.UserEntity;
import com.ape.exception.ResourceNotFoundException;
import com.ape.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {
        UserEntity user =  userDao.findByEmail(email);
        return new UserDetailsImpl(user);
    }
}
