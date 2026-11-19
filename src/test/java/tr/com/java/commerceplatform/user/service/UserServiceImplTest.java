package tr.com.java.commerceplatform.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit tests for the signup, login and verify flows in {@link UserServiceImpl}. */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String EMAIL = "ada@example.com";
    private static final String PASSWORD = "secret123";

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private SignupRequest buildSignup(Long roleId, String storeName) {
        return new SignupRequest("Ada", EMAIL, PASSWORD, roleId, storeName, null, null, null);
    }

    private User buildUser(String password) {
        User user = new User();
        user.setId(7L);
        user.setEmail(EMAIL);
        user.setPassword(password);
        return user;
    }

    @Test
    @DisplayName("Registers a normal user without creating a store")
    void shouldRegisterUserWithoutStore() {
        Role role = mock(Role.class);
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.save(buildSignup(2L, null));

        assertEquals(EMAIL, result.getEmail());
        assertSame(role, result.getRole());
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    @DisplayName("Creates a store for the user when store details are sent")
    void shouldCreateStoreForStoreOwner() {
        Role role = mock(Role.class);
        when(roleRepository.findById(3L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.save(buildSignup(3L, "Ada Store"));

        verify(storeRepository).save(any(Store.class));
    }

    @Test
    @DisplayName("Throws BadRequestException and saves nothing when the role id is missing")
    void shouldFailWhenRoleIdIsMissing() {
        SignupRequest request = buildSignup(null, null);

        assertThrows(BadRequestException.class, () -> userService.save(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Throws ConflictException and saves nothing when the email is already registered")
    void shouldFailWhenEmailIsAlreadyRegistered() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);
        SignupRequest request = buildSignup(2L, null);

        assertThrows(ConflictException.class, () -> userService.save(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when the role does not exist")
    void shouldFailWhenRoleDoesNotExist() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());
        SignupRequest request = buildSignup(99L, null);

        assertThrows(ResourceNotFoundException.class, () -> userService.save(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Returns the user when the email and password match")
    void shouldLoginWithCorrectCredentials() {
        User user = buildUser(PASSWORD);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        User result = userService.login(new LoginRequest(EMAIL, PASSWORD));

        assertSame(user, result);
    }

    @Test
    @DisplayName("Throws InvalidCredentialsException when the email is unknown")
    void shouldFailLoginWhenEmailIsUnknown() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    @Test
    @DisplayName("Throws InvalidCredentialsException when the password is wrong")
    void shouldFailLoginWhenPasswordIsWrong() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(buildUser(PASSWORD)));
        LoginRequest request = new LoginRequest(EMAIL, "wrong-password");

        assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
    }

    @Test
    @DisplayName("Returns the user when verifying an existing email")
    void shouldVerifyExistingUser() {
        User user = buildUser(PASSWORD);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertSame(user, userService.verify(EMAIL));
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when verifying an unknown email")
    void shouldFailVerifyWhenUserIsMissing() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.verify(EMAIL));
    }
}