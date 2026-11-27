package tr.com.java.commerceplatform.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import tr.com.java.commerceplatform.card.dto.CardRequest;
import tr.com.java.commerceplatform.card.dto.CardResponse;
import tr.com.java.commerceplatform.card.entity.Card;
import tr.com.java.commerceplatform.card.service.CardService;
import tr.com.java.commerceplatform.common.response.CommonResponse;
import tr.com.java.commerceplatform.user.entity.User;

import java.util.List;

/** REST endpoints for managing the saved payment cards of the logged-in user. */
@RestController
@RequestMapping("/user/card")
@RequiredArgsConstructor
@Tag(name = "Cards", description = "Manage the saved payment cards of the logged-in user")
public class CardController {

    private final CardService cardService;

    /**
     * Returns all saved cards of the logged-in user.
     *
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with the card list wrapped in a CommonResponse
     */
    @Operation(
            summary = "List my cards",
            description = "Returns all saved payment cards of the logged-in user. Requires a valid token.")
    @ApiResponse(responseCode = "200", description = "Cards returned")
    @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    @GetMapping
    public ResponseEntity<CommonResponse<List<CardResponse>>> getCards(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<CardResponse> cards = cardService.getAllByUser(user).stream()
                .map(CardResponse::from)
                .toList();
        return ResponseEntity.ok(CommonResponse.success(cards, "Cards retrieved successfully"));
    }

    /**
     * Saves a new payment card for the logged-in user.
     *
     * @param request        the card data to save
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 201 with the saved card wrapped in a CommonResponse
     */
    @Operation(
            summary = "Add a card",
            description = "Saves a new payment card for the logged-in user. Requires a valid token.")
    @ApiResponse(responseCode = "201", description = "Card saved")
    @ApiResponse(responseCode = "400", description = "The request body is missing or malformed")
    @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    @PostMapping
    public ResponseEntity<CommonResponse<CardResponse>> addCard(@RequestBody CardRequest request,
                                                                Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Card saved = cardService.save(user, request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(CardResponse.from(saved),
                        "Card created successfully", HttpStatus.CREATED));
    }

    /**
     * Deletes a saved card of the logged-in user.
     *
     * @param id             the id of the card to delete
     * @param authentication the authentication of the current user, filled in by Spring Security
     * @return HTTP 200 with an empty payload and a confirmation message
     */
    @Operation(
            summary = "Delete a card",
            description = "Deletes one of the logged-in user's saved cards. Requires a valid token.")
    @ApiResponse(responseCode = "200", description = "Card deleted")
    @ApiResponse(responseCode = "403", description = "The token is missing, invalid or expired")
    @ApiResponse(responseCode = "404", description = "The card does not exist or belongs to another user")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteCard(@PathVariable Long id,
                                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cardService.delete(user, id);
        return ResponseEntity.ok(CommonResponse.success(null, "Card deleted successfully"));
    }
}