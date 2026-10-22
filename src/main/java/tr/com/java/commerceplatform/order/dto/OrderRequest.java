package tr.com.java.commerceplatform.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long addressId;
    private String orderDate;
    private String cardNo;
    private String cardName;
    private Integer cardExpireMonth;
    private Integer cardExpireYear;
    private Integer cardCcv;
    private Double price;
    private List<OrderProductRequest> products;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductRequest {
        private Long productId;
        private Integer count;
        private String detail;
    }
}