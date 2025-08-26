package sportbet.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import sportbet.io.JacksonListMarketReader;
import sportbet.model.ParsedMarket;
import sportbet.model.RawMarket;

/**
 * Conversion service from official sources
 */
public class ConversionService {
    
    private final JacksonListMarketReader reader;
    private final MarketConverter converter;
    private final ObjectMapper jsonMapper;
    
    public ConversionService() {
        this.reader = new JacksonListMarketReader();
        this.converter = new MarketConverter();
        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    /**
     * Convert input file to output file
     */
    public void convertFile(String inputFilePath, String outputFilePath) {
        System.out.println("════════════════════════════════════════");
        System.out.println("       SPORTBET MARKET CONVERTER");
        System.out.println("════════════════════════════════════════");
        System.out.println("📁 Input:  " + inputFilePath);
        System.out.println("📁 Output: " + outputFilePath);
        System.out.println();
        
        try {
            // Read the file
            Path inputPath = Paths.get(inputFilePath);
            List<RawMarket> rawMarkets = reader.read(inputPath);
            System.out.println("✅ Loaded " + rawMarkets.size() + " raw markets");
            
            // Conversion
            List<ParsedMarket> parsedMarkets = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;
            
            for (int i = 0; i < rawMarkets.size(); i++) {
                RawMarket rawMarket = rawMarkets.get(i);
                try {
                    ParsedMarket parsedMarket = converter.convert(rawMarket);
                    parsedMarkets.add(parsedMarket);
                    successCount++;
                    
                    System.out.printf("✅ Market #%d: %s (Type ID: %s)%n", 
                        i + 1, rawMarket.getName(), parsedMarket.getMarket_type_id());
                    
                } catch (Exception e) {
                    failCount++;
                    System.out.printf("❌ Market #%d: %s - ERROR: %s%n", 
                        i + 1, rawMarket.getName(), e.getMessage());
                }
            }
            
            // Write the file
            File outputFile = new File(outputFilePath);
            jsonMapper.writeValue(outputFile, parsedMarkets);
            
            System.out.println();
            System.out.println("════════════════════════════════════════");
            System.out.println("             CONVERSION SUMMARY");
            System.out.println("════════════════════════════════════════");
            System.out.println("📊 Total markets: " + rawMarkets.size());
            System.out.println("✅ Successfully converted: " + successCount);
            System.out.println("❌ Failed: " + failCount);
            System.out.println("📁 Output file: " + outputFile.getAbsolutePath());
            System.out.println("💾 File size: " + String.format("%.2f KB", outputFile.length() / 1024.0));
            System.out.println("════════════════════════════════════════");
            

        } catch (IOException e) {
            System.out.println("❌ FATAL ERROR: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ UNEXPECTED ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Main entry point
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java sportbet.core.ConversionService <input-file> <output-file>");
            System.out.println("Example: java sportbet.core.ConversionService input.json output.json");
            System.exit(1);
        }
        
        ConversionService service = new ConversionService();
        service.convertFile(args[0], args[1]);
    }
}
