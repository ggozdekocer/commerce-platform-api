package tr.com.java.commerceplatform.card.dto;

import tr.com.java.commerceplatform.card.entity.Card;

/** Data a client sends when saving a payment card. */
public record CardRequest(
        String cardNo,
        String nameOnCard,
        Integer expireMonth,
        Integer expireYear) {

    /** Converts this request into a new Card entity (without id and user). */
    public Card toEntity() {
        Card card = new Card();
        card.setCardNo(cardNo);
        card.setNameOnCard(nameOnCard);
        card.setExpireMonth(expireMonth);
        card.setExpireYear(expireYear);
        return card;
    }
}