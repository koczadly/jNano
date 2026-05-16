package uk.oczadly.karl.jnano.model.currency;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

class RawDenomination implements Denomination {

    private final String displayName;

    RawDenomination(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getSymbol() {
        return "";
    }

    @Override
    public BigDecimal convertFromRaw(BigInteger rawAmount) {
        return new BigDecimal(rawAmount);
    }

    @Override
    public BigInteger convertToRaw(BigDecimal value) {
        try {
            return value.toBigIntegerExact();
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Raw value cannot have decimal precision.");
        }
    }

    @Override
    public String format(BigDecimal value, Locale locale) {
        return convertToRaw(value) + " " + getDisplayName();
    }

}
