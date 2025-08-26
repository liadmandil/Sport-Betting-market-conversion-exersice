package sportbet.validate;

import java.util.List;
import java.util.Objects;

import sportbet.errors.DomainException;
import sportbet.errors.ErrorCode;
import sportbet.model.RawMarket;
import sportbet.model.RawSelection;

/**
 * Class for validation of Raw Market data
 */
public class RawMarketValidator {

    /**
     * Parameters for Raw Market validation
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
     * Performs full validation on Raw Market
     * 
     * @param params validation parameters
     * @throws DomainException if data is invalid
     */
    public void validate(ValidationParams params) {
        RawMarket market = params.getRawMarket();
        boolean strict = params.isStrictMode();

        validateMarketName(market.getName(), strict);
        validateEventId(market.getEvent_id(), strict);
        validateSelections(market.getSelections(), strict);
    }

    /**
     * Validates market name
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
     * Validates event ID
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
     * Validates selections
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
     * Validates individual selection
     */
    private void validateSelection(RawSelection selection, int index, boolean strictMode) {
        if (selection == null) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection at index " + index + " cannot be null");
        }

        // Name validation
        if (selection.getName() == null || selection.getName().trim().isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection name at index " + index + " cannot be null or empty");
        }

        // Odds validation
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
