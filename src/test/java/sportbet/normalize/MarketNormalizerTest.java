package sportbet.normalize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests עבור MarketNormalizer
 */
class MarketNormalizerTest {

    private MarketNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new MarketNormalizer();
    }

    @Test
    @DisplayName("ניקוי שם selection עם הסרת מספרים")
    void cleanSelectionName_removeNumbers() {
        // Arrange
        MarketNormalizer.SelectionNameParams params = 
            new MarketNormalizer.SelectionNameParams("Team A +1.5", true, true);

        // Act
        String result = normalizer.cleanSelectionName(params);

        // Assert
        assertEquals("Team A", result);
    }

    @Test
    @DisplayName("ניקוי שם selection ללא הסרת מספרים")
    void cleanSelectionName_keepNumbers() {
        // Arrange
        MarketNormalizer.SelectionNameParams params = 
            new MarketNormalizer.SelectionNameParams("Team A +1.5", false, true);

        // Act
        String result = normalizer.cleanSelectionName(params);

        // Assert
        assertEquals("Team A +1.5", result);
    }

    @Test
    @DisplayName("ניקוי רווחים מיותרים")
    void cleanSelectionName_trimWhitespace() {
        // Arrange
        MarketNormalizer.SelectionNameParams params = 
            new MarketNormalizer.SelectionNameParams("  Team   A  ", false, true);

        // Act
        String result = normalizer.cleanSelectionName(params);

        // Assert
        assertEquals("Team A", result);
    }

    @Test
    @DisplayName("חילוץ ערך total מ-'over 2.5'")
    void extractTotalValue_over() {
        // Act
        String result = normalizer.extractTotalValue("over 2.5");

        // Assert
        assertEquals("2.5", result);
    }

    @Test
    @DisplayName("חילוץ ערך total מ-'under 1.5'")
    void extractTotalValue_under() {
        // Act
        String result = normalizer.extractTotalValue("under 1.5");

        // Assert
        assertEquals("1.5", result);
    }

    @Test
    @DisplayName("חילוץ ערך total - ברירת מחדל כאשר אין מספר")
    void extractTotalValue_noNumber() {
        // Act
        String result = normalizer.extractTotalValue("over");

        // Assert
        assertEquals("2.5", result); // ברירת מחדל
    }

    @Test
    @DisplayName("חילוץ ערך handicap מ-'Team A +1.5'")
    void extractHandicapValue_positive() {
        // Act
        String result = normalizer.extractHandicapValue("Team A +1.5");

        // Assert
        assertEquals("1.5", result);
    }

    @Test
    @DisplayName("חילוץ ערך handicap מ-'Team B -0.5'")
    void extractHandicapValue_negative() {
        // Act
        String result = normalizer.extractHandicapValue("Team B -0.5");

        // Assert
        assertEquals("-0.5", result);
    }

    @Test
    @DisplayName("חילוץ ערך handicap - ברירת מחדל כאשר אין מספר")
    void extractHandicapValue_noNumber() {
        // Act
        String result = normalizer.extractHandicapValue("Team A");

        // Assert
        assertEquals("0", result); // ברירת מחדל
    }

    @Test
    @DisplayName("נורמליזציה של שם market")
    void normalizeMarketName() {
        // Act
        String result = normalizer.normalizeMarketName("  TOTAL  ");

        // Assert
        assertEquals("total", result);
    }

    @Test
    @DisplayName("נורמליזציה של שם market - null")
    void normalizeMarketName_null() {
        // Act
        String result = normalizer.normalizeMarketName(null);

        // Assert
        assertEquals("", result);
    }

    @Test
    @DisplayName("נורמליזציה של selection לזיהוי")
    void normalizeSelectionForIdentification() {
        // Act
        String result = normalizer.normalizeSelectionForIdentification("Team A +1.5");

        // Assert
        assertEquals("team a", result);
    }

    @Test
    @DisplayName("נורמליזציה של selection עם מספרים עשרוניים")
    void normalizeSelectionForIdentification_decimals() {
        // Act
        String result = normalizer.normalizeSelectionForIdentification("over 2.5");

        // Assert
        assertEquals("over", result);
    }
}
