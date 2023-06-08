package com.ape.security.service;
import com.ape.dao.UserDao;
import com.ape.entity.UserEntity;
import com.ape.exception.ResourceNotFoundException;
import com.ape.utility.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {
        UserEntity user =  userDao.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE));
        return UserDetailsImpl.build(user);
    }
}
