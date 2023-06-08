package com.ape.mapper;

import com.ape.dto.UserDTO;
import com.ape.entity.RoleEntity;
import com.ape.entity.UserEntity;
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

    default Set<String> getRolesAsString(Set<RoleEntity> roles){
        return roles.stream().map(role -> role.getRoleName().toString()).collect(Collectors.toSet());
    }
}
