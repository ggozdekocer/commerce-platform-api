package tr.com.java.commerceplatform.card.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.java.commerceplatform.card.entity.Card;
import tr.com.java.commerceplatform.card.service.CardService;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** REST endpoints for managing the saved payment cards of the logged-in user. */
@RestController
@RequestMapping("/user/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * Returns all saved cards of the logged-in user.
     *
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with the card list wrapped in a CommonResponse
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<Card>>> getCards(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Card> cards = cardService.getAllByUser(user);
        return ResponseEntity.ok(CommonResponse.success(cards, "Cards retrieved successfully"));
    }

    /**
     * Saves a new payment card for the logged-in user.
     *
     * @param card           the card to save
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 201 with the saved card wrapped in a CommonResponse
     */
    @PostMapping
    public ResponseEntity<CommonResponse<Card>> addCard(@RequestBody Card card,
                                                        Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Card saved = cardService.save(user, card);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(saved, "Card created successfully", HttpStatus.CREATED));
    }

    /**
     * Deletes a saved card of the logged-in user.
     *
     * @param id             the id of the card to delete
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with an empty payload and a confirmation message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteCard(@PathVariable Long id,
                                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cardService.delete(user, id);
        return ResponseEntity.ok(CommonResponse.success(null, "Card deleted successfully"));
    }
}