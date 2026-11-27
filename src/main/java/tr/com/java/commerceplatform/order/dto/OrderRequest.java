package tr.com.java.commerceplatform.order.dto;

import java.util.List;

/**
 * Request body for creating an order.
 *
 * @param addressId       id of the delivery address
 * @param orderDate       date of the order
 * @param cardNo          number of the card used for payment
 * @param cardName        name on the card
 * @param cardExpireMonth expiry month of the card
 * @param cardExpireYear  expiry year of the card
 * @param cardCcv         security code of the card
 * @param price           total price of the order
 * @param products        the products and quantities in the order
 */
public record OrderRequest(
        Long addressId,
        String orderDate,
        String cardNo,
        String cardName,
        Integer cardExpireMonth,
        Integer cardExpireYear,
        Integer cardCcv,
        Double price,
        List<OrderProductRequest> products
) {

    /**
     * A single product line inside an order.
     *
     * @param productId id of the ordered product
     * @param count     number of items ordered
     * @param detail    extra detail such as color or size
     */
    public record OrderProductRequest(
            Long productId,
            Integer count,
            String detail
    ) {
    }
}