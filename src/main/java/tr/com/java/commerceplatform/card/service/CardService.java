package tr.com.java.commerceplatform.card.service;

import tr.com.java.commerceplatform.card.entity.Card;
import tr.com.java.commerceplatform.common.exception.ResourceNotFoundException;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** Business operations for the saved payment cards of a user. */
public interface CardService {

    /**
     * Returns all saved cards of a user.
     *
     * @param user the owner of the cards
     * @return the cards of the user, empty when there are none
     */
    List<Card> getAllByUser(User user);

    /**
     * Saves a new payment card for a user.
     *
     * @param user the owner of the card
     * @param card the card to save
     * @return the saved card
     */
    Card save(User user, Card card);

    /**
     * Deletes a saved card of a user.
     *
     * @param user the user who owns the card
     * @param id   the id of the card to delete
     * @throws ResourceNotFoundException if the card does not exist or belongs to another user
     */
    void delete(User user, Long id);
}