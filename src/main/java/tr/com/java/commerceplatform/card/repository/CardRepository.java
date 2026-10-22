package tr.com.java.commerceplatform.card.repository;

import tr.com.java.commerceplatform.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUserId(Long userId);
}

