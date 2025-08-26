package sportbet.normalize;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * מחלקה לניקוי ונורמליזציה של נתוני שוקים
 */
public class MarketNormalizer {

    /**
     * פרמטרים לניקוי שם selection
     */
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
     * פרמטרים לחילוץ מספרים
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
     * מנקה שם selection מסימנים ומספרים
     * 
     * @param params פרמטרי הניקוי
     * @return שם נקי
     */
    public String cleanSelectionName(SelectionNameParams params) {
        String name = params.getSelectionName();
        
        if (params.isRemoveNumbers()) {
            // הסרת מספרים וסימנים כמו +1.5, -2.5, וכו'
            name = name.replaceAll("[+\\-]?\\d+(?:\\.\\d+)?", "");
        }
        
        if (params.isTrimWhitespace()) {
            // הסרת רווחים מיותרים
            name = name.replaceAll("\\s+", " ").trim();
        }
        
        return name;
    }

    /**
     * מחלץ מספר מטקסט לפי pattern
     * 
     * @param params פרמטרי החילוץ
     * @return המספר שנמצא או ערך ברירת מחדל
     */
    public String extractNumber(NumberExtractionParams params) {
        Matcher matcher = params.getPattern().matcher(params.getText());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return params.getDefaultValue();
    }

    /**
     * מחלץ ערך total מתוך שם selection
     * דוגמה: "over 2.5" → "2.5"
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
     * מחלץ ערך handicap מתוך שם selection
     * דוגמה: "Team A +1.5" → "1.5"
     */
    public String extractHandicapValue(String selectionName) {
        NumberExtractionParams params = new NumberExtractionParams(
            selectionName,
            DECIMAL_PATTERN,
            "0"
        );
        String result = extractNumber(params);
        
        // הסרת סימן + אם קיים
        if (result.startsWith("+")) {
            result = result.substring(1);
        }
        
        return result;
    }

    /**
     * נורמליזציה של שם market
     */
    public String normalizeMarketName(String marketName) {
        if (marketName == null) return "";
        return marketName.trim().toLowerCase();
    }

    /**
     * נורמליזציה של שם selection לזיהוי
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
