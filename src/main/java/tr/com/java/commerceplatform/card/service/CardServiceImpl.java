package tr.com.java.commerceplatform.card.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.java.commerceplatform.card.entity.Card;
import tr.com.java.commerceplatform.card.repository.CardRepository;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** Default implementation of {@link CardService} backed by the card repository. */
@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    /** {@inheritDoc} */
    @Override
    public List<Card> getAllByUser(User user) {
        log.debug("Loading cards of user id={}", user.getId());
        return cardRepository.findByUserId(user.getId());
    }

    /** {@inheritDoc} */
    @Override
    public Card save(User user, Card card) {
        card.setUser(user);
        Card saved = cardRepository.save(card);
        log.info("Card saved for user id={}", user.getId());
        return saved;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the card does not exist or belongs to another user
     */
    @Override
    public void delete(User user, Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Card delete failed: card id={} not found", id);
                    return new ResourceNotFoundException("Card not found with id: " + id);
                });
        if (!card.getUser().getId().equals(user.getId())) {
            log.warn("User id={} tried to delete card id={} of another user", user.getId(), id);
            // Same message as a missing card, so other users' ids are not revealed
            throw new ResourceNotFoundException("Card not found with id: " + id);
        }
        cardRepository.delete(card);
        log.info("Card id={} deleted by user id={}", id, user.getId());
    }
}