package tr.com.java.commerceplatform.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Unit tests for {@link CardNumberMasker}. */
class CardNumberMaskerTest {

    @Test
    @DisplayName("Keeps only the last four digits of a plain card number")
    void shouldKeepLastFourDigits() {
        assertEquals("**** **** **** 1111", CardNumberMasker.mask("4111111111111111"));
    }

    @Test
    @DisplayName("Ignores spaces and dashes in the card number")
    void shouldIgnoreSpacesAndDashes() {
        assertEquals("**** **** **** 1111", CardNumberMasker.mask("4111 1111-1111 1111"));
    }

    @Test
    @DisplayName("Hides everything when the number has four digits or fewer")
    void shouldMaskShortNumbersCompletely() {
        assertEquals("****", CardNumberMasker.mask("1234"));
    }

    @Test
    @DisplayName("Returns null when no card number is given")
    void shouldReturnNullForNull() {
        assertNull(CardNumberMasker.mask(null));
    }
}