package tr.com.java.commerceplatform.card.service;

import tr.com.java.commerceplatform.card.entity.Card;
import tr.com.java.commerceplatform.user.entity.User;
import tr.com.java.commerceplatform.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements ICardService {

    private final CardRepository cardRepository;

    @Override
    public List<Card> getAllByUser(User user) {
        return cardRepository.findByUserId(user.getId());
    }

    @Override
    public Card save(User user, Card card) {
        card.setUser(user);
        return cardRepository.save(card);
    }

    @Override
    public void delete(User user, Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        if (!card.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        cardRepository.delete(card);
    }
}