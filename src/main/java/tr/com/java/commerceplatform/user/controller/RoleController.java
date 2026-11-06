package tr.com.java.commerceplatform.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Roles", description = "Roles a user can sign up with")
public class RoleController {

    private final RoleService roleService;

    /**
     * Returns all available user roles.
     *
     * @return HTTP 200 with the role list wrapped in a CommonResponse
     */
    @Operation(
            summary = "List all roles",
            description = "Returns the roles a user can choose when signing up. "
                    + "Use the id of a role as role_id in the signup request. No login required.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles returned")
    })
    @GetMapping("/roles")
    public ResponseEntity<CommonResponse<List<Role>>> getRoles() {
        List<Role> roles = roleService.getAll();
        return ResponseEntity.ok(CommonResponse.success(roles, "Roles retrieved successfully"));
    }
}