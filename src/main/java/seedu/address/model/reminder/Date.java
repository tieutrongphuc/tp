package seedu.address.model.reminder;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Represents a Reminder's date.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_CONSTRAINTS =
            "Date must be a valid date in 'yyyy-MM-dd' or 'd/M/yyyy' format, "
            + "optionally with a time in 'HH:mm' format.";

    // Formatters
    private static final List<DateTimeFormatter> DATETIME_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")
    );
    private static final List<DateTimeFormatter> DATE_ONLY_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
    );

    // Default Time for Date-Only Inputs
    private static final LocalTime DEFAULT_TIME = LocalTime.MAX;

    public final LocalDateTime value;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date string.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_CONSTRAINTS);
        this.value = parse(date);
    }

    /**
     * Checks if this date is upcoming (within the next 7 days from now).
     * A date is considered upcoming if it's not in the past and not more than 7 days in the future.
     *
     * @return true if the date is within the next 7 days, false otherwise
     */
    public boolean isUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysLater = now.plusDays(7);

        boolean result = !value.isBefore(now) && !value.isAfter(sevenDaysLater);
        System.out.println("Checking: " + value + " | Now: " + now + " | Result: " + result);
        return !value.isBefore(now) && !value.isAfter(sevenDaysLater);
    }

    /**
     * Parses the input string into a LocalDateTime using the defined formatters after validation.
     */
    private static LocalDateTime parse(String input) {
        for (DateTimeFormatter formatter : DATETIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }
        for (DateTimeFormatter formatter : DATE_ONLY_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                return date.atTime(DEFAULT_TIME);
            } catch (DateTimeParseException e) {
                // Continue to next format
            }
        }
        // This part should not be reachable if isValidDate passed
        throw new IllegalStateException("Internal parsing error: validated string failed to parse.");
    }

    /**
     * Returns true if a given string is a valid date.
     * A valid date is one that can be parsed by at least one of the defined formatters.
     */
    public static boolean isValidDate(String test) {
        if (test == null) {
            return false;
        }
        String trimmedTest = test.trim();

        for (DateTimeFormatter formatter : DATETIME_FORMATTERS) {
            try {
                LocalDateTime.parse(trimmedTest, formatter);
                return true; // Found a valid format
            } catch (DateTimeParseException e) {
                // Ignore and try the next format
            }
        }
        for (DateTimeFormatter formatter : DATE_ONLY_FORMATTERS) {
            try {
                LocalDate.parse(trimmedTest, formatter);
                return true; // Found a valid format
            } catch (DateTimeParseException e) {
                // Ignore and try the next format
            }
        }

        // None of the formats matched
        return false;
    }

    @Override
    public String toString() {
        if (value.toLocalTime().equals(DEFAULT_TIME)) {
            return value.format(DATE_ONLY_FORMATTERS.get(0)); // yyyy-MM-dd
        } else {
            return value.format(DATETIME_FORMATTERS.get(0)); // yyyy-MM-dd HH:mm
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Date)) {
            return false;
        }
        Date otherDate = (Date) other;
        return value.equals(otherDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
