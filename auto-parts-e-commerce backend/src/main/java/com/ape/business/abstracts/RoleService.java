package com.ape.business.abstracts;

import com.ape.entity.Role;
import com.ape.entity.enums.RoleType;
import com.ape.utility.DataResponse;

public interface RoleService {

    Role findByRoleName(RoleType roleType);
}
