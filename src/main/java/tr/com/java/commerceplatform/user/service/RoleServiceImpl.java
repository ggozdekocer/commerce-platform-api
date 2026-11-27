package tr.com.java.commerceplatform.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.user.entity.Role;
import tr.com.java.commerceplatform.user.repository.RoleRepository;

import java.util.List;

/** Default implementation of {@link RoleService} backed by the role repository. */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /** {@inheritDoc} */
    @Override
    public List<Role> getAll() {
        log.debug("Loading all roles");
        return roleRepository.findAll();
    }
}