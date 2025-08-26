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
     * Extract specifiers by market type
     */

    private Map<String, String> extractSpecifiers(RawMarket rawMarket, MarketType marketType) {
        Map<String, String> specifiers = new HashMap<>();
        
        switch (marketType.getSpecifierType()) {
            case NONE:
                return specifiers; // Empty Map for markets without specifiers
                
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
     * Create market_uid from event ID, market type and specifiers
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
     * Extract Total value (e.g., "2.5" from "over 2.5")
     */
    private String extractTotalValue(RawMarket rawMarket) {
        // Search for number in first selection name
        for (RawSelection selection : rawMarket.getSelections()) {
            String result = normalizer.extractTotalValue(selection.getName());
            if (!result.equals("2.5")) { // If not default value
                return result;
            }
        }
        return "2.5"; // Default value
    }
    
    /**
     * Extract Handicap value (e.g., "1.5" from "Team A +1.5")
     */
    private String extractHandicapValue(RawMarket rawMarket) {
        // Search for number with +/- in first selection name
        for (RawSelection selection : rawMarket.getSelections()) {
            String result = normalizer.extractHandicapValue(selection.getName());
            if (!result.equals("0")) { // If not default value
                return result;
            }
        }
        return "0"; // Default value
    }
    
    /**
     * Convert list of selections from RawSelection to ParsedSelection
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
     * Convert single selection from RawSelection to ParsedSelection
     */
    private ParsedSelection convertSelection(RawSelection rawSelection, MarketType marketType, String marketUid) {
        // Clean selection name (remove numbers and special characters for identification)
        String cleanName = cleanSelectionName(rawSelection.getName());
        
        // Find selection_type_id by cleaned name
        Optional<Integer> selectionTypeIdOpt = marketType.resolveSelectionTypeId(cleanName);
        
        if (selectionTypeIdOpt.isEmpty()) {
            throw new IllegalArgumentException(
                "Unknown selection name: '" + rawSelection.getName() + 
                "' (cleaned: '" + cleanName + "') for market type: " + marketType.name()
            );
        }
        
        // Create selection_uid = market_uid + "_" + selection_type_id
        String selectionUid = marketUid + "_" + selectionTypeIdOpt.get();
        
        return new ParsedSelection(
            selectionUid,
            selectionTypeIdOpt.get(),
            rawSelection.getOdds()
        );
    }
    
    /**
     * Clean selection name to remove numbers and special characters
     * For example: "Team A +1.5" -> "team a", "over 2.5" -> "over"
     */
    private String cleanSelectionName(String name) {
        String cleaned = name.toLowerCase().trim();
        
        // Remove numbers and special characters
        cleaned = cleaned.replaceAll("[+-]?\\d+(?:\\.\\d+)?", "").trim();
        
        // Remove extra spaces
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        
        return cleaned;
    }
}
