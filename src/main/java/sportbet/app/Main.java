package sportbet.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import sportbet.core.MarketConverter;
import sportbet.io.FilePathResolver;
import sportbet.io.JacksonListMarketReader;
import sportbet.model.ParsedMarket;
import sportbet.model.RawMarket;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar market-conversion.jar <filename>");
            System.out.println("  The file will be searched in input_files directory");
            System.out.println("  Output will be saved in output_files directory with '_output' suffix");
            System.exit(1);
        }

        try {
            // Ensure output directory exists
            FilePathResolver.ensureOutputDirectoryExists();
            
            // Resolve input and output paths
            String inputFilename = args[0];
            Path inputPath = FilePathResolver.resolveInputPath(inputFilename);
            Path outputPath = FilePathResolver.resolveOutputPath(inputFilename);
            
            System.out.println("=== Market Conversion ===");
            System.out.println("Input file: " + inputPath.toAbsolutePath());
            System.out.println("Output file: " + outputPath.toAbsolutePath());
            
            // Read input file
            JacksonListMarketReader reader = new JacksonListMarketReader();
            List<RawMarket> rawMarkets = reader.read(inputPath);
            
            System.out.println("=== Market Conversion ===");
            System.out.println("Loaded " + rawMarkets.size() + " markets:");
            

            // Convert markets
            MarketConverter converter = new MarketConverter();
            List<ParsedMarket> parsedMarkets = new ArrayList<>();
            
            for (int i = 0; i < rawMarkets.size(); i++) {
                RawMarket rawMarket = rawMarkets.get(i);
                System.out.println("Converting market #" + (i + 1) + ": " + rawMarket.getName());
                
                try {
                    ParsedMarket parsed = converter.convert(rawMarket);
                    parsedMarkets.add(parsed);
                    System.out.println("✅ Successfully converted - " + parsed.getMarket_uid());
                } catch (Exception e) {
                    System.out.println("❌ Conversion error: " + e.getMessage());
                }
            }
            
            // Write JSON output
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            
            mapper.writeValue(outputPath.toFile(), parsedMarkets);
            
            System.out.println("\n=== Summary ===");
            System.out.println("Converted " + parsedMarkets.size() + " out of " + rawMarkets.size() + " markets");
            System.out.println("JSON file saved to: " + outputPath.toAbsolutePath());
            
            // Print JSON content to console as well
            System.out.println("\n=== JSON File Content ===");
            System.out.println(mapper.writeValueAsString(parsedMarkets));
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
