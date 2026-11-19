package tr.com.java.commerceplatform.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Provides the password encoder used to hash and check user passwords. */
@Configuration
public class PasswordConfig {

    /** Creates a BCrypt encoder, which adds a random salt to every hash. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}