package tr.com.java.commerceplatform.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.card.entity.Card;
import tr.com.java.commerceplatform.card.repository.CardRepository;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** Default implementation of {@link CardService} backed by the card repository. */
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    /** {@inheritDoc} */
    @Override
    public List<Card> getAllByUser(User user) {
        return cardRepository.findByUserId(user.getId());
    }

    /** {@inheritDoc} */
    @Override
    public Card save(User user, Card card) {
        card.setUser(user);
        return cardRepository.save(card);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the card does not exist or belongs to another user
     */
    @Override
    public void delete(User user, Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        if (!card.getUser().getId().equals(user.getId())) {
            // Same message as a missing card, so other users' ids are not revealed
            throw new ResourceNotFoundException("Card not found with id: " + id);
        }
        cardRepository.delete(card);
    }
}