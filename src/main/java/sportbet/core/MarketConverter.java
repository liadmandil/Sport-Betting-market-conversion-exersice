package sportbet.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import sportbet.domain.MarketType;
import sportbet.model.ParsedMarket;
import sportbet.model.ParsedSelection;
import sportbet.model.RawMarket;
import sportbet.model.RawSelection;
import sportbet.normalize.MarketNormalizer;
import sportbet.uid.UidGenerator;

/**
 * Main class for converting markets from RawMarket to ParsedMarket
 * Includes dictionary for all market types and logic for extracting specifiers
 */
public class MarketConverter {
    
    private final UidGenerator uidGenerator;
    private final MarketNormalizer normalizer;
    
    public MarketConverter() {
        this.uidGenerator = new UidGenerator();
        this.normalizer = new MarketNormalizer();
    }
    
    /**
     * Converts RawMarket to ParsedMarket
     */
    public ParsedMarket convert(RawMarket rawMarket) {
        // Find market type by name
        Optional<MarketType> marketTypeOpt = MarketType.fromName(rawMarket.getName());
        
        if (marketTypeOpt.isEmpty()) {
            throw new IllegalArgumentException("Unknown market type: " + rawMarket.getName());
        }
        
        MarketType marketType = marketTypeOpt.get();
        
        // Extract specifiers based on market type
        Map<String, String> specifiers = extractSpecifiers(rawMarket, marketType);
        
        // Generate market_uid
        String marketUid = generateMarketUid(rawMarket.getEvent_id(), marketType.getTypeId(), specifiers);
        
        // Convert all selections
        List<ParsedSelection> parsedSelections = convertSelections(rawMarket.getSelections(), marketType, marketUid);
        
        // Create ParsedMarket
        return new ParsedMarket(
            marketUid,
            marketType.getTypeId(),
            specifiers,
            parsedSelections
        );
    }
    
    /**
     * חילוץ specifiers לפי סוג השוק
     */

    private Map<String, String> extractSpecifiers(RawMarket rawMarket, MarketType marketType) {
        Map<String, String> specifiers = new HashMap<>();
        
        switch (marketType.getSpecifierType()) {
            case NONE:
                return specifiers; // Map ריק לשוקים ללא specifiers
                
            case TOTAL:
                String totalValue = extractTotalValue(rawMarket);
                specifiers.put("total", totalValue);
                return specifiers;
                
            case HCP:
                String hcpValue = extractHandicapValue(rawMarket);
                specifiers.put("hcp", hcpValue);
                return specifiers;
                
            default:
                return specifiers;
        }
    }
    
    /**
     * יצירת market_uid מזהה אירוע, סוג שוק ו-specifiers
     */
    private String generateMarketUid(String eventId, String marketTypeId, Map<String, String> specifiers) {
        UidGenerator.MarketUidParams params = new UidGenerator.MarketUidParams(
            eventId, 
            marketTypeId, 
            specifiers
        );
        return UidGenerator.generateMarketUid(params);
    }
    
    /**
     * חילוץ ערך Total (למשל "2.5" מתוך "over 2.5")
     */
    private String extractTotalValue(RawMarket rawMarket) {
        // מחפש מספר בשם הראשון של selection
        for (RawSelection selection : rawMarket.getSelections()) {
            String result = normalizer.extractTotalValue(selection.getName());
            if (!result.equals("2.5")) { // אם לא ברירת מחדל
                return result;
            }
        }
        return "2.5"; // ברירת מחדל
    }
    
    /**
     * חילוץ ערך Handicap (למשל "1.5" מתוך "Team A +1.5")
     */
    private String extractHandicapValue(RawMarket rawMarket) {
        // מחפש מספר עם +/- בשם הראשון של selection
        for (RawSelection selection : rawMarket.getSelections()) {
            String result = normalizer.extractHandicapValue(selection.getName());
            if (!result.equals("0")) { // אם לא ברירת מחדל
                return result;
            }
        }
        return "0"; // ברירת מחדל
    }
    
    /**
     * המרת רשימת selections מ-RawSelection ל-ParsedSelection
     */
    private List<ParsedSelection> convertSelections(List<RawSelection> rawSelections, MarketType marketType, String marketUid) {
        List<ParsedSelection> parsedSelections = new ArrayList<>();
        
        for (RawSelection rawSelection : rawSelections) {
            ParsedSelection parsedSelection = convertSelection(rawSelection, marketType, marketUid);
            parsedSelections.add(parsedSelection);
        }
        
        return parsedSelections;
    }
    
    /**
     * המרת selection יחיד מ-RawSelection ל-ParsedSelection
     */
    private ParsedSelection convertSelection(RawSelection rawSelection, MarketType marketType, String marketUid) {
        // ניקוי שם הselection (הסרת מספרים וסימנים מיוחדים לצורך זיהוי)
        String cleanName = cleanSelectionName(rawSelection.getName());
        
        // מציאת selection_type_id לפי השם המנוקה
        Optional<Integer> selectionTypeIdOpt = marketType.resolveSelectionTypeId(cleanName);
        
        if (selectionTypeIdOpt.isEmpty()) {
            throw new IllegalArgumentException(
                "Unknown selection name: '" + rawSelection.getName() + 
                "' (cleaned: '" + cleanName + "') for market type: " + marketType.name()
            );
        }
        
        // יצירת selection_uid = market_uid + "_" + selection_type_id
        String selectionUid = marketUid + "_" + selectionTypeIdOpt.get();
        
        return new ParsedSelection(
            selectionUid,
            selectionTypeIdOpt.get(),
            rawSelection.getOdds()
        );
    }
    
    /**
     * ניקוי שם selection להסרת מספרים וסימנים מיוחדים
     * למשל: "Team A +1.5" -> "team a", "over 2.5" -> "over"
     */
    private String cleanSelectionName(String name) {
        String cleaned = name.toLowerCase().trim();
        
        // הסרת מספרים וסימנים מיוחדים
        cleaned = cleaned.replaceAll("[+-]?\\d+(?:\\.\\d+)?", "").trim();
        
        // הסרת רווחים מיותרים
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        
        return cleaned;
    }
}
