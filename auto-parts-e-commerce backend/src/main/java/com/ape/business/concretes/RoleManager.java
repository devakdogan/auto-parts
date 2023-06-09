package com.ape.business.concretes;

import com.ape.business.abstracts.RoleService;
import com.ape.entity.dao.RoleDao;
import com.ape.entity.concrete.RoleEntity;
import com.ape.entity.enums.RoleType;
import com.ape.exception.ResourceNotFoundException;
import com.ape.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleManager implements RoleService {

    private final RoleDao roleDao;
    @Override
    public RoleEntity findByRoleName(RoleType roleType) {
        return roleDao.findByRoleName(roleType).orElseThrow(()->
                new ResourceNotFoundException(String.format(
                        ErrorMessage.ROLE_NOT_FOUND_MESSAGE, roleType.name())));
    }
}
