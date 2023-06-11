package com.ape.controller;

import com.ape.business.abstracts.UserService;
import com.ape.entity.dto.UserDTO;
import com.ape.entity.dto.request.UserUpdateRequest;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import com.ape.entity.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users with filter and page")
    public ResponseEntity<PageImpl<UserDTO>> getAllUsersWithFilterAndPage(@RequestParam(value = "q",required = false)String query,
                                                               @RequestParam(value = "role",required = false) RoleType role,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size, @RequestParam("sort") String prop,
                                                               @RequestParam(value = "direction", required = false, defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        PageImpl<UserDTO> userDTOPage = userService.getAllUsersWithFilterAndPage(query,role,pageable);
        return ResponseEntity.ok(userDTOPage);
    }

    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Get currently logged user")
    public ResponseEntity<UserDTO> getUser() {
        UserDTO userDTO = userService.getPrincipal();
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);

    }

    @GetMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Getting user with ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "The user can update their account")
    public ResponseEntity<Response> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserDTO userDTO= userService.updateUser(userUpdateRequest);
        Response response = new DataResponse<>(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE, true,userDTO);
        return ResponseEntity.ok(response);
    }
}
