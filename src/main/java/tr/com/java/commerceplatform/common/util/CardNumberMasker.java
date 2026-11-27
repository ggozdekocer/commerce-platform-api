package tr.com.java.commerceplatform.common.util;

/** Hides all but the last four digits of a card number before it is stored or returned. */
public final class CardNumberMasker {

    private static final String MASK_PREFIX = "**** **** **** ";
    private static final String FULLY_MASKED = "****";

    private CardNumberMasker() {
    }

    /**
     * Masks a card number so that only the last four digits stay visible.
     *
     * @param cardNo the card number, may contain spaces or dashes
     * @return the masked number such as "**** **** **** 1111", or null when no number is given
     */
    public static String mask(String cardNo) {
        if (cardNo == null) {
            return null;
        }
        String digits = cardNo.replaceAll("\\D", "");
        if (digits.length() <= 4) {
            return FULLY_MASKED;
        }
        return MASK_PREFIX + digits.substring(digits.length() - 4);
    }
}