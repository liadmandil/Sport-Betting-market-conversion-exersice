package sportbet.uid;

import java.util.Map;
import java.util.Objects;

/**
 * Dedicated class for creating UIDs for games and selections
 */
public class UidGenerator {
    
    /**
     * Parameters for creating Market UID
     */
    public static class MarketUidParams {
        private final String eventId;
        private final String marketTypeId;
        private final Map<String, String> specifiers;

        public MarketUidParams(String eventId, String marketTypeId, Map<String, String> specifiers) {
            this.eventId = Objects.requireNonNull(eventId, "eventId cannot be null");
            this.marketTypeId = Objects.requireNonNull(marketTypeId, "marketTypeId cannot be null");
            this.specifiers = specifiers != null ? specifiers : Map.of();
        }

        public String getEventId() {
            return eventId;
        }

        public String getMarketTypeId() {
            return marketTypeId;
        }

        public Map<String, String> getSpecifiers() {
            return specifiers;
        }
    }

    /**
     * Parameters for creating Selection UID
     */
    public static class SelectionUidParams {
        private final String marketUid;
        private final int selectionTypeId;

        public SelectionUidParams(String marketUid, int selectionTypeId) {
            this.marketUid = Objects.requireNonNull(marketUid, "marketUid cannot be null");
            this.selectionTypeId = selectionTypeId;
        }

        public String getMarketUid() {
            return marketUid;
        }

        public int getSelectionTypeId() {
            return selectionTypeId;
        }
    }
    
    /**
     * Creates market_uid according to format: {event_id}_{market_type_id}_{specifier_value}
     * 
     * @param params Parameters for creating Market UID
     * @return Unique UID for game
     */
    public static String generateMarketUid(MarketUidParams params) {
        StringBuilder uid = new StringBuilder();
        uid.append(params.getEventId())
           .append("_")
           .append(params.getMarketTypeId());
        
        // Add specifier value if exists
        if (!params.getSpecifiers().isEmpty()) {
            for (String value : params.getSpecifiers().values()) {
                if (value != null && !value.trim().isEmpty()) {
                    uid.append("_").append(value);
                    break; // Only the first value
                }
            }
        }
        
        return uid.toString();
    }
    
    /**
     * Creates selection_uid according to format: {market_uid}_{selection_type_id}
     * 
     * @param params Parameters for creating Selection UID
     * @return Unique UID for selection
     */
    public static String generateSelectionUid(SelectionUidParams params) {
        return params.getMarketUid() + "_" + params.getSelectionTypeId();
    }

    // Backward compatibility methods
    /**
     * @deprecated Use generateMarketUid(MarketUidParams) instead
     */
    @Deprecated
    public static String generateMarketUid(String eventId, String marketTypeId, Map<String, String> specifiers) {
        return generateMarketUid(new MarketUidParams(eventId, marketTypeId, specifiers));
    }
    
    /**
     * @deprecated Use generateSelectionUid(SelectionUidParams) instead
     */
    @Deprecated
    public static String generateSelectionUid(String marketUid, int selectionTypeId) {
        return generateSelectionUid(new SelectionUidParams(marketUid, selectionTypeId));
    }
    
    /**
     * Validates market UID
     */
    public static boolean isValidMarketUid(String marketUid) {
        if (marketUid == null || marketUid.trim().isEmpty()) {
            return false;
        }
        
        // Basic format: eventId_marketTypeId or eventId_marketTypeId_specifier
        String[] parts = marketUid.split("_");
        return parts.length >= 2 && parts.length <= 3;
    }
    
    /**
     * Validates selection UID
     */
    public static boolean isValidSelectionUid(String selectionUid) {
        if (selectionUid == null || selectionUid.trim().isEmpty()) {
            return false;
        }
        
        // Must contain at least market_uid + selection_type_id
        String[] parts = selectionUid.split("_");
        if (parts.length < 3) {
            return false;
        }
        
        // Last part should be a number (selection_type_id)
        try {
            Integer.parseInt(parts[parts.length - 1]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Extract event ID from market UID
     */
    public static String extractEventId(String marketUid) {
        if (!isValidMarketUid(marketUid)) {
            throw new IllegalArgumentException("Invalid market UID: " + marketUid);
        }
        
        return marketUid.split("_")[0];
    }
    
    /**
     * Extract market type ID from market UID
     */
    public static String extractMarketTypeId(String marketUid) {
        if (!isValidMarketUid(marketUid)) {
            throw new IllegalArgumentException("Invalid market UID: " + marketUid);
        }
        
        return marketUid.split("_")[1];
    }
    
    /**
     * Extract specifier value from market UID if exists
     */
    public static String extractSpecifierValue(String marketUid) {
        if (!isValidMarketUid(marketUid)) {
            throw new IllegalArgumentException("Invalid market UID: " + marketUid);
        }
        
        String[] parts = marketUid.split("_");
        return parts.length >= 3 ? parts[2] : null;
    }
}
