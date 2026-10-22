package tr.com.java.commerceplatform.user.service;

import tr.com.java.commerceplatform.auth.dto.LoginRequest;
import tr.com.java.commerceplatform.auth.dto.SignupRequest;
import tr.com.java.commerceplatform.user.entity.User;

public interface IUserService {
    User save(SignupRequest request);
    User login(LoginRequest request);
    User verify(String email);
}