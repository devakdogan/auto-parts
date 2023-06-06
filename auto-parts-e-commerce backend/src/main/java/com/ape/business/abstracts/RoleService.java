package com.ape.business.abstracts;

import com.ape.entity.Role;
import com.ape.entity.enums.RoleType;

public interface RoleService {

    Role findByRoleName(RoleType roleType);
}
