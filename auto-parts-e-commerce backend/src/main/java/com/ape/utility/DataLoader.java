package com.ape.utility;

import com.ape.dao.RoleDao;
import com.ape.entity.RoleEntity;
import com.ape.entity.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleDao roleDao;

    @Override
    public void run(String... args) throws Exception {


        roleDao.save(new RoleEntity( 1L, RoleType.USER));
        roleDao.save(new RoleEntity(2L,RoleType.ADMIN));
    }
}

