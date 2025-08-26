package sportbet.uid;

import java.util.Map;
import java.util.Objects;

/**
 * מחלקה ייעודית ליצירת UIDs למשחקים ו-selections
 */
public class UidGenerator {
    
    /**
     * פרמטרים ליצירת Market UID
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
     * פרמטרים ליצירת Selection UID
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
     * יוצר market_uid לפי הפורמט: {event_id}_{market_type_id}_{specifier_value}
     * 
     * @param params פרמטרים ליצירת Market UID
     * @return UID ייחודי למשחק
     */
    public static String generateMarketUid(MarketUidParams params) {
        StringBuilder uid = new StringBuilder();
        uid.append(params.getEventId())
           .append("_")
           .append(params.getMarketTypeId());
        
        // הוספת specifier value אם קיים
        if (!params.getSpecifiers().isEmpty()) {
            for (String value : params.getSpecifiers().values()) {
                if (value != null && !value.trim().isEmpty()) {
                    uid.append("_").append(value);
                    break; // רק הערך הראשון
                }
            }
        }
        
        return uid.toString();
    }
    
    /**
     * יוצר selection_uid לפי הפורמט: {market_uid}_{selection_type_id}
     * 
     * @param params פרמטרים ליצירת Selection UID
     * @return UID ייחודי לבחירה
     */
    public static String generateSelectionUid(SelectionUidParams params) {
        return params.getMarketUid() + "_" + params.getSelectionTypeId();
    }

    // Backward compatibility methods
    /**
     * @deprecated השתמש ב-generateMarketUid(MarketUidParams) במקום
     */
    @Deprecated
    public static String generateMarketUid(String eventId, String marketTypeId, Map<String, String> specifiers) {
        return generateMarketUid(new MarketUidParams(eventId, marketTypeId, specifiers));
    }
    
    /**
     * @deprecated השתמש ב-generateSelectionUid(SelectionUidParams) במקום
     */
    @Deprecated
    public static String generateSelectionUid(String marketUid, int selectionTypeId) {
        return generateSelectionUid(new SelectionUidParams(marketUid, selectionTypeId));
    }
    
    /**
     * בודק תקינות של market UID
     */
    public static boolean isValidMarketUid(String marketUid) {
        if (marketUid == null || marketUid.trim().isEmpty()) {
            return false;
        }
        
        // פורמט בסיסי: eventId_marketTypeId או eventId_marketTypeId_specifier
        String[] parts = marketUid.split("_");
        return parts.length >= 2 && parts.length <= 3;
    }
    
    /**
     * בודק תקינות של selection UID
     */
    public static boolean isValidSelectionUid(String selectionUid) {
        if (selectionUid == null || selectionUid.trim().isEmpty()) {
            return false;
        }
        
        // צריך להכיל לפחות market_uid + selection_type_id
        String[] parts = selectionUid.split("_");
        if (parts.length < 3) {
            return false;
        }
        
        // החלק האחרון צריך להיות מספר (selection_type_id)
        try {
            Integer.parseInt(parts[parts.length - 1]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * מחלץ event ID מ-market UID
     */
    public static String extractEventId(String marketUid) {
        if (!isValidMarketUid(marketUid)) {
            throw new IllegalArgumentException("Invalid market UID: " + marketUid);
        }
        
        return marketUid.split("_")[0];
    }
    
    /**
     * מחלץ market type ID מ-market UID
     */
    public static String extractMarketTypeId(String marketUid) {
        if (!isValidMarketUid(marketUid)) {
            throw new IllegalArgumentException("Invalid market UID: " + marketUid);
        }
        
        return marketUid.split("_")[1];
    }
    
    /**
     * מחלץ specifier value מ-market UID אם קיים
     */
    public static String extractSpecifierValue(String marketUid) {
        if (!isValidMarketUid(marketUid)) {
            throw new IllegalArgumentException("Invalid market UID: " + marketUid);
        }
        
        String[] parts = marketUid.split("_");
        return parts.length >= 3 ? parts[2] : null;
    }
}
