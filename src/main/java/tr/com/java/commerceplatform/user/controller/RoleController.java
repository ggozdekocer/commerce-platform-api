package tr.com.java.commerceplatform.user.controller;

import tr.com.java.commerceplatform.user.entity.Role;
import tr.com.java.commerceplatform.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleService.getAll();
    }
}