package tr.com.java.commerceplatform.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.java.commerceplatform.user.entity.User;

@Entity
@Table(name = "store")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String taxNo;        // tax_no
    private String bankAccount; // bank_account

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
