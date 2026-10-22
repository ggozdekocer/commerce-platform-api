package tr.com.java.commerceplatform.card.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.java.commerceplatform.user.entity.User;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNo;       // card_no
    private String nameOnCard;   // name_on_card
    private Integer expireMonth; // expire_month
    private Integer expireYear;  // expire_year

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
