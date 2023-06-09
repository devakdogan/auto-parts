package com.ape.business.abstracts;

import com.ape.entity.concrete.RoleEntity;
import com.ape.entity.enums.RoleType;

public interface RoleService {

    RoleEntity findByRoleName(RoleType roleType);
}
