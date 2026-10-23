package tr.com.java.commerceplatform.user.service;

import tr.com.java.commerceplatform.user.entity.Role;
import tr.com.java.commerceplatform.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}