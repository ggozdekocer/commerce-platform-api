package tr.com.java.commerceplatform.user.service;

import tr.com.java.commerceplatform.auth.dto.LoginRequest;
import tr.com.java.commerceplatform.auth.dto.SignupRequest;
import tr.com.java.commerceplatform.user.entity.Role;
import tr.com.java.commerceplatform.store.entity.Store;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.user.repository.RoleRepository;
import tr.com.java.commerceplatform.store.repository.StoreRepository;
import tr.com.java.commerceplatform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;

    @Override
    public User save(SignupRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(role);

        User savedUser = userRepository.save(user);

        if (request.getStoreName() != null) {
            Store store = new Store();
            store.setName(request.getStoreName());
            store.setPhone(request.getStorePhone());
            store.setTaxNo(request.getStoreTaxNo());
            store.setBankAccount(request.getStoreBankAccount());
            store.setUser(savedUser);
            storeRepository.save(store);
        }

        return savedUser;
    }

    @Override
    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public User verify(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}