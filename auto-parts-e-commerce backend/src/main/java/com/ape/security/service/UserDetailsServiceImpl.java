package com.ape.security.service;
import com.ape.dao.UserDao;
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

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {

        User user =  userDao.findByEmail(email);
        return UserDetailsImpl.build(user);
    }
}
