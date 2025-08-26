package sportbet.validate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sportbet.errors.DomainException;
import sportbet.errors.ErrorCode;
import sportbet.model.RawMarket;
import sportbet.model.RawSelection;

/**
 * JUnit tests עבור RawMarketValidator
 */
class RawMarketValidatorTest {

    private RawMarketValidator validator;
    private RawMarket validMarket;

    @BeforeEach
    void setUp() {
        validator = new RawMarketValidator();
        
        // יצירת market תקין לבדיקות
        validMarket = new RawMarket();
        validMarket.setName("1x2");
        validMarket.setEvent_id("123456");
        
        List<RawSelection> selections = new ArrayList<>();
        
        RawSelection selection1 = new RawSelection();
        selection1.setName("Team A");
        selection1.setOdds(1.65);
        selections.add(selection1);
        
        RawSelection selection2 = new RawSelection();
        selection2.setName("Draw");
        selection2.setOdds(3.2);
        selections.add(selection2);
        
        validMarket.setSelections(selections);
    }

    @Test
    @DisplayName("ולידציה של market תקין - אמור לעבור בהצלחה")
    void validate_validMarket_success() {
        // Arrange
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, false);

        // Act & Assert - לא אמור לזרוק exception
        assertDoesNotThrow(() -> validator.validate(params));
    }

    @Test
    @DisplayName("שגיאה כאשר שם השוק הוא null")
    void validate_nullMarketName_throwsException() {
        // Arrange
        validMarket.setName(null);
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
        assertTrue(exception.getMessage().contains("Market name cannot be null"));
    }

    @Test
    @DisplayName("שגיאה כאשר שם השוק ריק")
    void validate_emptyMarketName_throwsException() {
        // Arrange
        validMarket.setName("");
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("שגיאה כאשר Event ID הוא null")
    void validate_nullEventId_throwsException() {
        // Arrange
        validMarket.setEvent_id(null);
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
        assertTrue(exception.getMessage().contains("Event ID cannot be null"));
    }

    @Test
    @DisplayName("שגיאה כאשר אין selections")
    void validate_noSelections_throwsException() {
        // Arrange
        validMarket.setSelections(null);
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
        assertTrue(exception.getMessage().contains("at least one selection"));
    }

    @Test
    @DisplayName("שגיאה כאשר selections ריק")
    void validate_emptySelections_throwsException() {
        // Arrange
        validMarket.setSelections(new ArrayList<>());
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("שגיאה כאשר odds של selection קטן מ-1")
    void validate_invalidOdds_throwsException() {
        // Arrange
        validMarket.getSelections().get(0).setOdds(0.5);
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
        assertTrue(exception.getMessage().contains("must be greater than 1.0"));
    }

    @Test
    @DisplayName("ולידציה במצב strict - שם ארוך מדי")
    void validate_strictMode_longMarketName_throwsException() {
        // Arrange
        String longName = "a".repeat(101); // יותר מ-100 תווים
        validMarket.setName(longName);
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, true);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
        assertTrue(exception.getMessage().contains("too long"));
    }

    @Test
    @DisplayName("ולידציה במצב strict - Event ID לא חיובי")
    void validate_strictMode_negativeEventId_throwsException() {
        // Arrange
        validMarket.setEvent_id("-123");
        RawMarketValidator.ValidationParams params = 
            new RawMarketValidator.ValidationParams(validMarket, true);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, 
            () -> validator.validate(params));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getCode());
        assertTrue(exception.getMessage().contains("must be positive"));
    }
}
