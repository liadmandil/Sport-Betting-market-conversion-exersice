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

// Converts raw markets to parsed markets with UIDs and specifiers
public class MarketConverter {
    
    private final MarketNormalizer normalizer;
    
    public MarketConverter() {
        this.normalizer = new MarketNormalizer();
    }
    
    // Main conversion method
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
    
    // Extract specifiers based on market type
    private Map<String, String> extractSpecifiers(RawMarket rawMarket, MarketType marketType) {
        Map<String, String> specifiers = new HashMap<>();
        
        switch (marketType.getSpecifierType()) {
            case NONE:
                return specifiers;
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
    
    // Generate market UID
    private String generateMarketUid(String eventId, String marketTypeId, Map<String, String> specifiers) {
        UidGenerator.MarketUidParams params = new UidGenerator.MarketUidParams(
            eventId, 
            marketTypeId, 
            specifiers
        );
        return UidGenerator.generateMarketUid(params);
    }
    
    // Extract total value from selection names
    private String extractTotalValue(RawMarket rawMarket) {
        for (RawSelection selection : rawMarket.getSelections()) {
            String result = normalizer.extractTotalValue(selection.getName());
            if (!result.equals("0")) {
                return result;
            }
        }
        return "0";
    }
    
    // Extract handicap value from selection names
    private String extractHandicapValue(RawMarket rawMarket) {
        for (RawSelection selection : rawMarket.getSelections()) {
            String result = normalizer.extractHandicapValue(selection.getName());
            if (!result.equals("0")) {
                return result;
            }
        }
        return "0";
    }
    
    // Convert all selections
    private List<ParsedSelection> convertSelections(List<RawSelection> rawSelections, MarketType marketType, String marketUid) {
        List<ParsedSelection> parsedSelections = new ArrayList<>();
        
        for (RawSelection rawSelection : rawSelections) {
            ParsedSelection parsedSelection = convertSelection(rawSelection, marketType, marketUid);
            parsedSelections.add(parsedSelection);
        }
        
        return parsedSelections;
    }
    
    // Convert single selection
    private ParsedSelection convertSelection(RawSelection rawSelection, MarketType marketType, String marketUid) {
        String cleanName = cleanSelectionName(rawSelection.getName());
        Optional<Integer> selectionTypeIdOpt = marketType.resolveSelectionTypeId(cleanName);
        
        if (selectionTypeIdOpt.isEmpty()) {
            throw new IllegalArgumentException(
                "Unknown selection name: '" + rawSelection.getName() + 
                "' (cleaned: '" + cleanName + "') for market type: " + marketType.name()
            );
        }
        
        String selectionUid = marketUid + "_" + selectionTypeIdOpt.get();
        
        return new ParsedSelection(
            selectionUid,
            String.valueOf(selectionTypeIdOpt.get()),
            rawSelection.getOdds()
        );
    }
    
    // Remove numbers and special characters from selection name
    private String cleanSelectionName(String name) {
        String cleaned = name.toLowerCase().trim();
        cleaned = cleaned.replaceAll("[+-]?\\d+(?:\\.\\d+)?", "").trim();
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        return cleaned;
    }
}
