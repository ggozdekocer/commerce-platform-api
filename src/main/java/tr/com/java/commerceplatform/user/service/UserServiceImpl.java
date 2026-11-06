package tr.com.java.commerceplatform.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.auth.dto.LoginRequest;
import tr.com.java.commerceplatform.auth.dto.SignupRequest;
import tr.com.java.commerceplatform.common.exception.BadRequestException;
import tr.com.java.commerceplatform.common.exception.ConflictException;
import tr.com.java.commerceplatform.common.exception.InvalidCredentialsException;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.store.entity.Store;
import tr.com.java.commerceplatform.store.repository.StoreRepository;
import tr.com.java.commerceplatform.user.entity.Role;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.user.repository.RoleRepository;
import tr.com.java.commerceplatform.user.repository.UserRepository;

/** Default implementation of {@link UserService} backed by the user, role and store repositories. */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;

    /**
     * {@inheritDoc}
     *
     * @throws BadRequestException       if the role id is missing
     * @throws ConflictException         if the email is already registered
     * @throws ResourceNotFoundException if the requested role does not exist
     */
    @Override
    public User save(SignupRequest request) {
        log.info("Signup requested with roleId={}", request.roleId());

        if (request.roleId() == null) {
            throw new BadRequestException("Role id is required");
        }

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Signup failed: email is already registered");
            throw new ConflictException("Email is already registered");
        }

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.roleId()));

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(role);

        User savedUser = userRepository.save(user);
        log.info("User registered with id={}", savedUser.getId());

        if (request.storeName() != null) {
            Store store = new Store();
            store.setName(request.storeName());
            store.setPhone(request.storePhone());
            store.setTaxNo(request.storeTaxNo());
            store.setBankAccount(request.storeBankAccount());
            store.setUser(savedUser);
            storeRepository.save(store);
            log.info("Store created for user id={}", savedUser.getId());
        }

        return savedUser;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidCredentialsException if the email is unknown or the password is wrong
     */
    @Override
    public User login(LoginRequest request) {
        log.info("Login attempt received");

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Login failed: no user found for the given email");
                    return new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
                });

        if (!user.getPassword().equals(request.password())) {
            log.warn("Login failed: wrong password for user id={}", user.getId());
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
        }

        log.info("User id={} logged in", user.getId());
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public User verify(String email) {
        log.debug("Verifying session for an existing user");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}