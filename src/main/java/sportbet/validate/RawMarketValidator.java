package sportbet.validate;

import java.util.List;
import java.util.Objects;

import sportbet.errors.DomainException;
import sportbet.errors.ErrorCode;
import sportbet.model.RawMarket;
import sportbet.model.RawSelection;

/**
 * מחלקה לולידציה של נתוני Raw Markets
 */
public class RawMarketValidator {

    /**
     * פרמטרים לולידציה של Raw Market
     */
    public static class ValidationParams {
        private final RawMarket rawMarket;
        private final boolean strictMode;

        public ValidationParams(RawMarket rawMarket, boolean strictMode) {
            this.rawMarket = Objects.requireNonNull(rawMarket, "rawMarket cannot be null");
            this.strictMode = strictMode;
        }

        public RawMarket getRawMarket() {
            return rawMarket;
        }

        public boolean isStrictMode() {
            return strictMode;
        }
    }

    /**
     * מבצע ולידציה מלאה על Raw Market
     * 
     * @param params פרמטרי הולידציה
     * @throws DomainException אם הנתונים לא תקינים
     */
    public void validate(ValidationParams params) {
        RawMarket market = params.getRawMarket();
        boolean strict = params.isStrictMode();

        validateMarketName(market.getName(), strict);
        validateEventId(market.getEvent_id(), strict);
        validateSelections(market.getSelections(), strict);
    }

    /**
     * בודק תקינות שם השוק
     */
    private void validateMarketName(String name, boolean strictMode) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market name cannot be null or empty");
        }

        if (strictMode && name.length() > 100) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market name too long (max 100 characters)");
        }
    }

    /**
     * בודק תקינות מזהה האירוע
     */
    private void validateEventId(String eventId, boolean strictMode) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Event ID cannot be null or empty");
        }

        if (strictMode) {
            try {
                long id = Long.parseLong(eventId);
                if (id <= 0) {
                    throw new DomainException(ErrorCode.VALIDATION_ERROR, "Event ID must be positive");
                }
            } catch (NumberFormatException e) {
                throw new DomainException(ErrorCode.VALIDATION_ERROR, "Event ID must be a valid number");
            }
        }
    }

    /**
     * בודק תקינות הselections
     */
    private void validateSelections(List<RawSelection> selections, boolean strictMode) {
        if (selections == null || selections.isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market must have at least one selection");
        }

        if (strictMode && selections.size() > 50) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Too many selections (max 50)");
        }

        for (int i = 0; i < selections.size(); i++) {
            RawSelection selection = selections.get(i);
            validateSelection(selection, i, strictMode);
        }
    }

    /**
     * בודק תקינות selection בודד
     */
    private void validateSelection(RawSelection selection, int index, boolean strictMode) {
        if (selection == null) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection at index " + index + " cannot be null");
        }

        // בדיקת שם
        if (selection.getName() == null || selection.getName().trim().isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection name at index " + index + " cannot be null or empty");
        }

        // בדיקת odds
        if (selection.getOdds() == null) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection odds at index " + index + " cannot be null");
        }

        double odds = selection.getOdds();
        if (odds <= 1.0) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection odds at index " + index + " must be greater than 1.0");
        }

        if (strictMode && odds > 1000.0) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection odds at index " + index + " too high (max 1000.0)");
        }
    }
}
