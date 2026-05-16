package uk.oczadly.karl.jnano.model.currency;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

public class ExponentDenomination implements Denomination {

    private static final int MAX_DECIMAL_PLACES = 6;

    private final String displayName;
    private final String symbol;
    private final int exponent;


    public ExponentDenomination(int exponent, String displayName, String symbol) {
        if (exponent < 0 || exponent > 38)
            throw new IllegalArgumentException("Exponent must be between 0 and 38.");

        this.displayName = Objects.requireNonNull(displayName, "display name");
        this.symbol = symbol;
        this.exponent = exponent;
    }


    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public final String getSymbol() {
        return symbol;
    }

    public final int getExponent() {
        return exponent;
    }

    @Override
    public final BigDecimal convertFromRaw(BigInteger rawAmount) {
        return new BigDecimal(rawAmount)
                .movePointLeft(getExponent())
                .stripTrailingZeros();
    }

    @Override
    public final BigInteger convertToRaw(BigDecimal amount) {
        try {
            return amount.movePointRight(getExponent())
                    .stripTrailingZeros()
                    .toBigIntegerExact();
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Amount has greater precision than denomination supports.");
        }
    }

    @Override
    public String format(BigDecimal value, Locale locale) {
        String valueString = formatValue(value, locale);
        return getSymbol() != null
                ? getSymbol() + valueString
                : valueString + " " + getDisplayName();
    }


    private String formatValue(BigDecimal value, Locale locale) {
        int digits = Math.min(MAX_DECIMAL_PLACES, getExponent());

        BigDecimal scaledValue = value.setScale(digits, RoundingMode.DOWN);

        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(locale));
        df.setMaximumFractionDigits(digits);
        df.setGroupingUsed(true);

        if (value.compareTo(scaledValue) != 0) {
            df.setMinimumFractionDigits(digits);
            return df.format(scaledValue) + '…';
        } else {
            return df.format(scaledValue);
        }
    }

}
