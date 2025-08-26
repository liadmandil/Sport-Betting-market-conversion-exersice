package sportbet.validate;

import java.util.List;
import java.util.Objects;

import sportbet.errors.DomainException;
import sportbet.errors.ErrorCode;
import sportbet.model.ParsedMarket;
import sportbet.model.ParsedSelection;
import sportbet.uid.UidGenerator;

/**
 * Class for validation of Parsed Market data
 */
public class ParsedMarketValidator {

    /**
     * Parameters for Parsed Market validation
     */
    public static class ValidationParams {
        private final ParsedMarket parsedMarket;
        private final boolean validateUids;

        public ValidationParams(ParsedMarket parsedMarket, boolean validateUids) {
            this.parsedMarket = Objects.requireNonNull(parsedMarket, "parsedMarket cannot be null");
            this.validateUids = validateUids;
        }

        public ParsedMarket getParsedMarket() {
            return parsedMarket;
        }

        public boolean isValidateUids() {
            return validateUids;
        }
    }

    /**
     * Performs full validation on Parsed Market
     * 
     * @param params validation parameters
     * @throws DomainException if data is invalid
     */
    public void validate(ValidationParams params) {
        ParsedMarket market = params.getParsedMarket();
        boolean validateUids = params.isValidateUids();

        validateMarketUid(market.getMarket_uid(), validateUids);
        validateMarketTypeId(market.getMarket_type_id());
        validateSpecifiers(market.getSpecifiers());
        validateSelections(market.getSelections(), market.getMarket_uid(), validateUids);
    }

    /**
     * Validates Market UID
     */
    private void validateMarketUid(String marketUid, boolean validateFormat) {
        if (marketUid == null || marketUid.trim().isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market UID cannot be null or empty");
        }

        if (validateFormat && !UidGenerator.isValidMarketUid(marketUid)) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Invalid Market UID format: " + marketUid);
        }
    }

    /**
     * Validates Market Type ID
     */
    private void validateMarketTypeId(String marketTypeId) {
        if (marketTypeId == null || marketTypeId.trim().isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market Type ID cannot be null or empty");
        }

        try {
            int id = Integer.parseInt(marketTypeId);
            if (id <= 0) {
                throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market Type ID must be positive");
            }
        } catch (NumberFormatException e) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market Type ID must be a valid number");
        }
    }

    /**
     * Validates Specifiers
     */
    private void validateSpecifiers(java.util.Map<String, String> specifiers) {
        if (specifiers == null) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Specifiers cannot be null");
        }

        // Check that all keys and values are valid
        for (java.util.Map.Entry<String, String> entry : specifiers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key == null || key.trim().isEmpty()) {
                throw new DomainException(ErrorCode.VALIDATION_ERROR, "Specifier key cannot be null or empty");
            }

            if (value == null || value.trim().isEmpty()) {
                throw new DomainException(ErrorCode.VALIDATION_ERROR, "Specifier value cannot be null or empty");
            }
        }
    }

    /**
     * Validates Selections
     */
    private void validateSelections(List<ParsedSelection> selections, String marketUid, boolean validateUids) {
        if (selections == null || selections.isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, "Market must have at least one selection");
        }

        for (int i = 0; i < selections.size(); i++) {
            ParsedSelection selection = selections.get(i);
            validateSelection(selection, i, marketUid, validateUids);
        }
    }

    /**
     * Validates individual Selection
     */
    private void validateSelection(ParsedSelection selection, int index, String marketUid, boolean validateUids) {
        if (selection == null) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection at index " + index + " cannot be null");
        }

        // בדיקת Selection UID
        String selectionUid = selection.getSelection_uid();
        if (selectionUid == null || selectionUid.trim().isEmpty()) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection UID at index " + index + " cannot be null or empty");
        }

        if (validateUids) {
            if (!UidGenerator.isValidSelectionUid(selectionUid)) {
                throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                    "Invalid Selection UID format at index " + index + ": " + selectionUid);
            }

            // בדיקה שה-Selection UID מתחיל עם Market UID
            if (!selectionUid.startsWith(marketUid)) {
                throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                    "Selection UID at index " + index + " does not match Market UID");
            }
        }

        // בדיקת Selection Type ID
        int selectionTypeId = selection.getSelection_type_id();
        if (selectionTypeId <= 0) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Selection Type ID at index " + index + " must be positive");
        }

        // בדיקת Decimal Odds
        double odds = selection.getDecimal_odds();
        if (odds <= 1.0) {
            throw new DomainException(ErrorCode.VALIDATION_ERROR, 
                "Decimal odds at index " + index + " must be greater than 1.0");
        }
    }
}
