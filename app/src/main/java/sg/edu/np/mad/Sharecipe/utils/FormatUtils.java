package sg.edu.np.mad.Sharecipe.utils;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Duration;

import java.util.Locale;

import java9.util.Optional;

/**
 * Helper class that contain various common formatting method for ease of use.
 */
public class FormatUtils {

    /**
     * Safely convert string to integer.
     *
     * @param value Target string to convert.
     * @return The integer or null, safely wrapper in {@link Optional} container.
     */
    @NotNull
    public static Optional<Integer> convertToInt(@Nullable String value) {
        if (value == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Safely convert string to double.
     *
     * @param value Target string to convert.
     * @return The integer or null, safely wrapper in {@link Optional} container.
     */
    @NotNull
    public static Optional<Double> convertToDouble(@Nullable String value) {
        if (value == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Converts duration object to string `HH:MM`.
     *
     * @param duration  Target duration to parse.
     * @return Nicely formatted string representing the duration.
     */
    @NotNull
    public static String parseDurationShort(@Nullable Duration duration) {
        if (duration == null) {
            return "00:00";
        }

        return String.format(Locale.ENGLISH, "%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart());
    }

    /**
     * Converts duration object to string `HH hours MM minutes`.
     *
     * @param duration  Target duration to parse.
     * @return Nicely formatted string representing the duration.
     */
    @NotNull
    public static String parseDurationLong(@Nullable Duration duration) {
        if (duration == null) {
            return "0 minutes";
        }

        // More than an hour
        if (duration.toHours() > 0) {
            return String.format(Locale.ENGLISH, "%d hours %d minutes",
                    duration.toHours(),
                    duration.toMinutesPart());
        }

        return String.format(Locale.ENGLISH, "%d minutes",
                duration.toMinutes());
    }
}
