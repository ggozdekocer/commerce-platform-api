package tr.com.java.commerceplatform.order.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.java.commerceplatform.user.entity.User;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long addressId;      // address_id
    private String orderDate;    // order_date
    private Double price;
    private String cardNo;       // card_no
    private String cardName;     // card_name
    private Integer cardExpireMonth; // card_expire_month
    private Integer cardExpireYear;  // card_expire_year

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
