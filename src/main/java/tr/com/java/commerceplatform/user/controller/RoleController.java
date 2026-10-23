package tr.com.java.commerceplatform.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.user.entity.Role;
import tr.com.java.commerceplatform.user.service.RoleService;

import java.util.List;

/** REST endpoints for reading user roles. */
@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Returns all available user roles.
     *
     * @return HTTP 200 with the role list wrapped in a CommonResponse
     */
    @GetMapping("/roles")
    public ResponseEntity<CommonResponse<List<Role>>> getRoles() {
        List<Role> roles = roleService.getAll();
        return ResponseEntity.ok(CommonResponse.success(roles, "Roles retrieved successfully"));
    }
}