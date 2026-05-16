package uk.oczadly.karl.jnano.model.currency;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/**
 * An interface representing a denomination (or "unit") of Nano currency.
 *
 * <p>The following standard denominations are provided as constants: {@link #NANO}, {@link #RAW}.
 */
public interface Denomination {

    /**
     * Represents the primitive raw unit.
     */
    Denomination RAW = ofRaw("raw");

    /**
     * Represents the base Nano unit (where one unit = 10<sup>30</sup> raw).
     */
    Denomination NANO = ofExponent(30, "Nano", "Ӿ");



    String getDisplayName();

    String getSymbol();

    BigDecimal convertFromRaw(BigInteger rawAmount);

    BigInteger convertToRaw(BigDecimal amount);

    String format(BigDecimal value, Locale locale);

    default String format(BigDecimal value) {
        return format(value, Locale.ROOT);
    }


    /**
     * Creates a primitive raw {@link Denomination} with a custom name.
     *
     * @param displayName the human-readable name of the unit
     * @return the constructed denomination
     * @throws NullPointerException if {@code displayName} is {@code null}
     */
    static Denomination ofRaw(String displayName) {
        return new RawDenomination(displayName);
    }

    /**
     * Creates a {@link Denomination} where one whole unit is equal to 10<sup>x</sup> raw, where <i>x</i> is the passed {@code exponent}.
     *
     * @param exponent    the power-of-ten relating one unit to raw (must be in the range 0-38)
     * @param displayName the human-readable name of the unit
     * @param symbol      the symbol, or {@code null} if there isn't one
     * @return the constructed denomination
     * @throws IllegalArgumentException if {@code exponent} is outside the range {@code [0, 38]}
     * @throws NullPointerException     if {@code displayName} is {@code null}
     */
    static Denomination ofExponent(int exponent, String displayName, String symbol) {
        return new ExponentDenomination(exponent, displayName, symbol);
    }

    /**
     * Creates a {@link Denomination} where one whole unit is equal to 10<sup>x</sup> raw, where <i>x</i> is the passed {@code exponent}.
     *
     * @param exponent    the power-of-ten relating one unit to raw (must be in the range 0-38)
     * @param displayName the human-readable name of the unit
     * @return the constructed denomination
     * @throws IllegalArgumentException if {@code exponent} is outside the range {@code [0, 38]}
     * @throws NullPointerException     if {@code displayName} is {@code null}
     */
    static Denomination ofExponent(int exponent, String displayName) {
        return new ExponentDenomination(exponent, displayName, null);
    }

}
