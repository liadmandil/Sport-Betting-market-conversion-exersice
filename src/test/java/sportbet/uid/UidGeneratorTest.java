package sportbet.uid;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests עבור UidGenerator
 */
class UidGeneratorTest {

    @Test
    @DisplayName("יצירת Market UID עם specifiers")
    void generateMarketUid_withSpecifiers() {
        // Arrange
        UidGenerator.MarketUidParams params = new UidGenerator.MarketUidParams(
            "123456", 
            "18", 
            Map.of("total", "2.5")
        );

        // Act
        String result = UidGenerator.generateMarketUid(params);

        // Assert
        assertEquals("123456_18_2.5", result);
    }

    @Test
    @DisplayName("יצירת Market UID ללא specifiers")
    void generateMarketUid_withoutSpecifiers() {
        // Arrange
        UidGenerator.MarketUidParams params = new UidGenerator.MarketUidParams(
            "123456", 
            "1", 
            Map.of()
        );

        // Act
        String result = UidGenerator.generateMarketUid(params);

        // Assert
        assertEquals("123456_1", result);
    }

    @Test
    @DisplayName("יצירת Selection UID")
    void generateSelectionUid_success() {
        // Arrange
        UidGenerator.SelectionUidParams params = new UidGenerator.SelectionUidParams(
            "123456_18_2.5", 
            12
        );

        // Act
        String result = UidGenerator.generateSelectionUid(params);

        // Assert
        assertEquals("123456_18_2.5_12", result);
    }

    @Test
    @DisplayName("שגיאה כאשר eventId הוא null")
    void generateMarketUid_nullEventId_throwsException() {
        // Assert
        assertThrows(NullPointerException.class, () -> {
            new UidGenerator.MarketUidParams(null, "18", Map.of());
        });
    }

    @Test
    @DisplayName("שגיאה כאשר marketTypeId הוא null")
    void generateMarketUid_nullMarketTypeId_throwsException() {
        // Assert
        assertThrows(NullPointerException.class, () -> {
            new UidGenerator.MarketUidParams("123456", null, Map.of());
        });
    }

    @Test
    @DisplayName("שגיאה כאשר marketUid הוא null ב-SelectionUidParams")
    void generateSelectionUid_nullMarketUid_throwsException() {
        // Assert
        assertThrows(NullPointerException.class, () -> {
            new UidGenerator.SelectionUidParams(null, 12);
        });
    }

    @Test
    @DisplayName("בדיקת תקינות Market UID - תקין")
    void isValidMarketUid_valid() {
        assertTrue(UidGenerator.isValidMarketUid("123456_18"));
        assertTrue(UidGenerator.isValidMarketUid("123456_18_2.5"));
    }

    @Test
    @DisplayName("בדיקת תקינות Market UID - לא תקין")
    void isValidMarketUid_invalid() {
        assertFalse(UidGenerator.isValidMarketUid(null));
        assertFalse(UidGenerator.isValidMarketUid(""));
        assertFalse(UidGenerator.isValidMarketUid("123456"));
        assertFalse(UidGenerator.isValidMarketUid("123456_18_2.5_extra"));
    }

    @Test
    @DisplayName("בדיקת תקינות Selection UID - תקין")
    void isValidSelectionUid_valid() {
        assertTrue(UidGenerator.isValidSelectionUid("123456_18_12"));
        assertTrue(UidGenerator.isValidSelectionUid("123456_18_2.5_12"));
    }

    @Test
    @DisplayName("בדיקת תקינות Selection UID - לא תקין")
    void isValidSelectionUid_invalid() {
        assertFalse(UidGenerator.isValidSelectionUid(null));
        assertFalse(UidGenerator.isValidSelectionUid(""));
        assertFalse(UidGenerator.isValidSelectionUid("123456_18"));
        assertFalse(UidGenerator.isValidSelectionUid("123456_18_abc"));
    }

    @Test
    @DisplayName("חילוץ Event ID מ-Market UID")
    void extractEventId_success() {
        assertEquals("123456", UidGenerator.extractEventId("123456_18"));
        assertEquals("123456", UidGenerator.extractEventId("123456_18_2.5"));
    }

    @Test
    @DisplayName("חילוץ Market Type ID מ-Market UID")
    void extractMarketTypeId_success() {
        assertEquals("18", UidGenerator.extractMarketTypeId("123456_18"));
        assertEquals("18", UidGenerator.extractMarketTypeId("123456_18_2.5"));
    }

    @Test
    @DisplayName("חילוץ Specifier Value מ-Market UID")
    void extractSpecifierValue_success() {
        assertNull(UidGenerator.extractSpecifierValue("123456_18"));
        assertEquals("2.5", UidGenerator.extractSpecifierValue("123456_18_2.5"));
    }
}
