package sportbet.normalize;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Normalizes and cleans market data
public class MarketNormalizer {

    // Parameters for cleaning selection names
    public static class SelectionNameParams {
        private final String selectionName;
        private final boolean removeNumbers;
        private final boolean trimWhitespace;

        public SelectionNameParams(String selectionName, boolean removeNumbers, boolean trimWhitespace) {
            this.selectionName = Objects.requireNonNull(selectionName, "selectionName cannot be null");
            this.removeNumbers = removeNumbers;
            this.trimWhitespace = trimWhitespace;
        }

        public String getSelectionName() {
            return selectionName;
        }

        public boolean isRemoveNumbers() {
            return removeNumbers;
        }

        public boolean isTrimWhitespace() {
            return trimWhitespace;
        }
    }

    /**
     * Parameters for number extraction
     */
    public static class NumberExtractionParams {
        private final String text;
        private final Pattern pattern;
        private final String defaultValue;

        public NumberExtractionParams(String text, Pattern pattern, String defaultValue) {
            this.text = Objects.requireNonNull(text, "text cannot be null");
            this.pattern = Objects.requireNonNull(pattern, "pattern cannot be null");
            this.defaultValue = defaultValue;
        }

        public String getText() {
            return text;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    // Patterns for common number extractions
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("([+-]?\\d+(?:\\.\\d+)?)");
    private static final Pattern POSITIVE_DECIMAL_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)");

    /**
     * Cleans selection name from symbols and numbers
     * 
     * @param params cleaning parameters
     * @return clean name
     */
    public String cleanSelectionName(SelectionNameParams params) {
        String name = params.getSelectionName();
        
        if (params.isRemoveNumbers()) {
            // Remove numbers and symbols like +1.5, -2.5, etc.
            name = name.replaceAll("[+\\-]?\\d+(?:\\.\\d+)?", "");
        }
        
        if (params.isTrimWhitespace()) {
            // Remove extra whitespaces
            name = name.replaceAll("\\s+", " ").trim();
        }
        
        return name;
    }

    /**
     * Extracts number from text according to pattern
     * 
     * @param params extraction parameters
     * @return the found number or default value
     */
    public String extractNumber(NumberExtractionParams params) {
        Matcher matcher = params.getPattern().matcher(params.getText());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return params.getDefaultValue();
    }

    /**
     * Extracts total value from selection name
     * Example: "over 2.5" → "2.5"
     */
    public String extractTotalValue(String selectionName) {
        NumberExtractionParams params = new NumberExtractionParams(
            selectionName, 
            POSITIVE_DECIMAL_PATTERN, 
            "2.5"
        );
        return extractNumber(params);
    }

    /**
     * Extracts handicap value from selection name with sign preserved
     * Example: "Team A +1.5" → "+1.5", "Team B -0.5" → "-0.5"
     */
    public String extractHandicapValue(String selectionName) {
        NumberExtractionParams params = new NumberExtractionParams(
            selectionName,
            DECIMAL_PATTERN,
            "0"
        );
        String result = extractNumber(params);
        
        // Keep the sign as is - don't remove + sign
        return result;
    }

    /**
     * Normalization of market name
     */
    public String normalizeMarketName(String marketName) {
        if (marketName == null) return "";
        return marketName.trim().toLowerCase();
    }

    /**
     * Normalization of selection name for identification
     */
    public String normalizeSelectionForIdentification(String selectionName) {
        SelectionNameParams params = new SelectionNameParams(
            selectionName,
            true,  // remove numbers
            true   // trim whitespace
        );
        return cleanSelectionName(params).toLowerCase();
    }
}
