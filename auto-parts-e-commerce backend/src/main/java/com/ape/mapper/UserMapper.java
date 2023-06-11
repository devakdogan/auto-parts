package com.ape.mapper;

import com.ape.entity.dto.UserDTO;
import com.ape.entity.concrete.RoleEntity;
import com.ape.entity.concrete.UserEntity;
import com.ape.entity.dto.UserDeleteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = ProductMapper.class)
public interface UserMapper {

    @Mapping(target = "roles",expression = "java(getRolesAsString(user.getRoles()))")
    UserDTO entityToDTO(UserEntity user);

    List<UserDTO> entityListToDTOList(List<UserEntity> userList);
    UserDeleteDTO entityToUserDeleteDTO(UserEntity user);

    default Set<String> getRolesAsString(Set<RoleEntity> roles){
        return roles.stream().map(role -> role.getRoleName().toString()).collect(Collectors.toSet());
    }
}
