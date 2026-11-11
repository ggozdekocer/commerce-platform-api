package tr.com.java.commerceplatform.card.dto;

import tr.com.java.commerceplatform.card.entity.Card;

/** Data returned to the client for a saved payment card. */
public record CardResponse(
        Long id,
        String cardNo,
        String nameOnCard,
        Integer expireMonth,
        Integer expireYear) {

    /** Builds a response from a Card entity, leaving out the user. */
    public static CardResponse from(Card card) {
        return new CardResponse(
                card.getId(),
                card.getCardNo(),
                card.getNameOnCard(),
                card.getExpireMonth(),
                card.getExpireYear());
    }
}