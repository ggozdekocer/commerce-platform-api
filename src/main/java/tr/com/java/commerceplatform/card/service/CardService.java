package tr.com.java.commerceplatform.card.service;

import tr.com.java.commerceplatform.card.entity.Card;
import tr.com.java.commerceplatform.user.entity.User;
import java.util.List;

public interface CardService {
    List<Card> getAllByUser(User user);
    Card save(User user, Card card);
    void delete(User user, Long id);
}