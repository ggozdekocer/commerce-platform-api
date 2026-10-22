package tr.com.java.commerceplatform.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private Long roleId;
    private String storeName;
    private String storePhone;
    private String storeTaxNo;
    private String storeBankAccount;
}
