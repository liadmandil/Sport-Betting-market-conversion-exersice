package sportbet.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import sportbet.model.ParsedMarket;
import sportbet.model.ParsedSelection;
import sportbet.model.RawMarket;
import sportbet.model.RawSelection;

/**
 * Test class for MarketConverter
 */
public class MarketConverterTest {
    
    public static void main(String[] args) {
        MarketConverter converter = new MarketConverter();
        List<ParsedMarket> allParsedMarkets = new ArrayList<>();
        
        System.out.println("════════════════════════════════════════");
        System.out.println("        MARKET CONVERTER TEST");
        System.out.println("════════════════════════════════════════");
        
        // Test all market types
        allParsedMarkets.add(testOneXTwo(converter));
        allParsedMarkets.add(testTotal(converter));
        allParsedMarkets.add(testHandicap(converter));
        allParsedMarkets.add(testBTTS(converter));
        
        // Export to JSON file
        exportToJson(allParsedMarkets);
        
        System.out.println("════════════════════════════════════════");
        System.out.println("     ALL TESTS COMPLETED SUCCESSFULLY");
        System.out.println("════════════════════════════════════════");
    }
    
    private static ParsedMarket testOneXTwo(MarketConverter converter) {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│             1x2 MARKET TEST             │");
        System.out.println("└─────────────────────────────────────────┘");
        
        RawMarket rawMarket = new RawMarket();
        rawMarket.setName("1x2");
        rawMarket.setEvent_id("123456");
        rawMarket.setSelections(Arrays.asList(
            createSelection("Team A", 1.65),
            createSelection("draw", 3.2),
            createSelection("Team B", 2.6)
        ));
        
        try {
            ParsedMarket parsed = converter.convert(rawMarket);
            
            System.out.println("✅ SUCCESS");
            System.out.println("📊 Market Type ID: " + parsed.getMarket_type_id());
            System.out.println("🆔 Market UID: " + parsed.getMarket_uid());
            System.out.println("⚙️  Specifiers: " + parsed.getSpecifiers());
            System.out.println("🎯 Selections Count: " + parsed.getSelections().size());
            
            for (int i = 0; i < parsed.getSelections().size(); i++) {
                ParsedSelection sel = parsed.getSelections().get(i);
                System.out.printf("   %d. Selection ID: %d, UID: %s, Odds: %.2f%n", 
                    i + 1, sel.getSelection_type_id(), sel.getSelection_uid(), sel.getDecimal_odds());
            }
            
            return parsed;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            return null;
        }
    }
    
    private static ParsedMarket testTotal(MarketConverter converter) {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│            TOTAL MARKET TEST            │");
        System.out.println("└─────────────────────────────────────────┘");
        
        RawMarket rawMarket = new RawMarket();
        rawMarket.setName("Total");
        rawMarket.setEvent_id("123456");
        rawMarket.setSelections(Arrays.asList(
            createSelection("over 2.5", 1.85),
            createSelection("under 2.5", 1.95)
        ));
        
        try {
            ParsedMarket parsed = converter.convert(rawMarket);
            
            System.out.println("✅ SUCCESS");
            System.out.println("📊 Market Type ID: " + parsed.getMarket_type_id());
            System.out.println("🆔 Market UID: " + parsed.getMarket_uid());
            System.out.println("⚙️  Specifiers: " + parsed.getSpecifiers());
            System.out.println("🎯 Selections Count: " + parsed.getSelections().size());
            
            for (int i = 0; i < parsed.getSelections().size(); i++) {
                ParsedSelection sel = parsed.getSelections().get(i);
                System.out.printf("   %d. Selection ID: %d, UID: %s, Odds: %.2f%n", 
                    i + 1, sel.getSelection_type_id(), sel.getSelection_uid(), sel.getDecimal_odds());
            }
            
            return parsed;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            return null;
        }
    }
    
    private static ParsedMarket testHandicap(MarketConverter converter) {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│          HANDICAP MARKET TEST           │");
        System.out.println("└─────────────────────────────────────────┘");
        
        RawMarket rawMarket = new RawMarket();
        rawMarket.setName("Handicap");
        rawMarket.setEvent_id("123456");
        rawMarket.setSelections(Arrays.asList(
            createSelection("Team A +1.5", 1.8),
            createSelection("Team B -1.5", 2.0)
        ));
        
        try {
            ParsedMarket parsed = converter.convert(rawMarket);
            
            System.out.println("✅ SUCCESS");
            System.out.println("📊 Market Type ID: " + parsed.getMarket_type_id());
            System.out.println("🆔 Market UID: " + parsed.getMarket_uid());
            System.out.println("⚙️  Specifiers: " + parsed.getSpecifiers());
            System.out.println("🎯 Selections Count: " + parsed.getSelections().size());
            
            for (int i = 0; i < parsed.getSelections().size(); i++) {
                ParsedSelection sel = parsed.getSelections().get(i);
                System.out.printf("   %d. Selection ID: %d, UID: %s, Odds: %.2f%n", 
                    i + 1, sel.getSelection_type_id(), sel.getSelection_uid(), sel.getDecimal_odds());
            }
            
            return parsed;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            return null;
        }
    }
    
    private static ParsedMarket testBTTS(MarketConverter converter) {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│             BTTS MARKET TEST            │");
        System.out.println("└─────────────────────────────────────────┘");
        
        RawMarket rawMarket = new RawMarket();
        rawMarket.setName("Both teams to score");
        rawMarket.setEvent_id("123456");
        rawMarket.setSelections(Arrays.asList(
            createSelection("Yes", 1.7),
            createSelection("No", 2.1)
        ));
        
        try {
            ParsedMarket parsed = converter.convert(rawMarket);
            
            System.out.println("✅ SUCCESS");
            System.out.println("📊 Market Type ID: " + parsed.getMarket_type_id());
            System.out.println("🆔 Market UID: " + parsed.getMarket_uid());
            System.out.println("⚙️  Specifiers: " + parsed.getSpecifiers());
            System.out.println("🎯 Selections Count: " + parsed.getSelections().size());
            
            for (int i = 0; i < parsed.getSelections().size(); i++) {
                ParsedSelection sel = parsed.getSelections().get(i);
                System.out.printf("   %d. Selection ID: %d, UID: %s, Odds: %.2f%n", 
                    i + 1, sel.getSelection_type_id(), sel.getSelection_uid(), sel.getDecimal_odds());
            }
            
            return parsed;
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Export results to JSON file
     */
    private static void exportToJson(List<ParsedMarket> markets) {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│           EXPORTING TO JSON             │");
        System.out.println("└─────────────────────────────────────────┘");
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            
            // Create output file
            File outputFile = new File("converted_markets_output.json");
            mapper.writeValue(outputFile, markets);
            
            System.out.println("✅ SUCCESS");
            System.out.println("📁 File saved: " + outputFile.getAbsolutePath());
            System.out.println("📊 Markets exported: " + markets.size());
            
            // Print JSON to console
            String jsonString = mapper.writeValueAsString(markets);
            System.out.println("\n🔍 JSON Content:");
            System.out.println("─".repeat(50));
            System.out.println(jsonString);
            System.out.println("─".repeat(50));
            
        } catch (IOException e) {
            System.out.println("❌ ERROR: Failed to export JSON - " + e.getMessage());
        }
    }
    
    private static RawSelection createSelection(String name, double odds) {
        RawSelection selection = new RawSelection();
        selection.setName(name);
        selection.setOdds(odds);
        return selection;
    }
}
