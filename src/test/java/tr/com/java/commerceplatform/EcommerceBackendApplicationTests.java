package tr.com.java.commerceplatform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/** Checks that the whole Spring application context starts. */
@SpringBootTest(properties = "jwt.secret=test-only-key-test-only-key-test-only-key-1234")
class EcommerceBackendApplicationTests {

	@Test
	@DisplayName("Starts the Spring application context")
	void contextLoads() {
		// the test passes when the context starts without an exception
	}
}