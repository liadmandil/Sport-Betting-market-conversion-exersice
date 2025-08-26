package sportbet.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sportbet.errors.MissingFileException;

/**
 * Utility class for resolving input and output file paths automatically.
 * Supports automatic directory resolution for input_files and output_files.
 */
public class FilePathResolver {
    
    private static final String INPUT_DIR = "input_files";
    private static final String OUTPUT_DIR = "output_files";
    private static final String OUTPUT_SUFFIX = "_output";
    

    /**
     * Resolves the input file path by looking in the input_files directory.
     * If the file doesn't exist in input_files, treats the filename as an absolute path.
     * 
     * @param filename The input filename (with or without path)
     * @return Path to the input file
     * @throws MissingFileException if the file doesn't exist
     */

    public static Path resolveInputPath(String filename) throws MissingFileException {
        // If it's already a full path, use it as is
        Path directPath = Paths.get(filename);
        if (Files.exists(directPath)) {
            return directPath;
        }
        
        // Try to find in input_files directory
        Path inputDirPath = Paths.get(INPUT_DIR, filename);
        if (Files.exists(inputDirPath)) {
            return inputDirPath;
        }
        
        // If neither exists, throw exception
        throw new MissingFileException("File not found: " + filename + 
            " (checked both direct path and " + INPUT_DIR + " directory)");
    }
    
    /**
     * Resolves the output file path by creating it in the output_files directory.
     * Automatically adds "_output" suffix before the file extension.
     * 
     * @param inputFilename The original input filename
     * @return Path for the output file in OUTPUT_FILES directory
     */
    public static Path resolveOutputPath(String inputFilename) {
        // Extract just the filename without any directory path
        String baseFilename = Paths.get(inputFilename).getFileName().toString();
        
        // Add output suffix before the extension
        String outputFilename = addOutputSuffix(baseFilename);
        
        // Create path in OUTPUT_FILES directory
        return Paths.get(OUTPUT_DIR, outputFilename);
    }
    
    /**
     * Adds "_output" suffix before the file extension.
     * Example: "markets.json" becomes "markets_output.json"
     * 
     * @param filename The original filename
     * @return Filename with output suffix
     */
    private static String addOutputSuffix(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // No extension, just add suffix
            return filename + OUTPUT_SUFFIX;
        } else {
            // Add suffix before extension
            String nameWithoutExtension = filename.substring(0, lastDotIndex);
            String extension = filename.substring(lastDotIndex);
            return nameWithoutExtension + OUTPUT_SUFFIX + extension;
        }
    }
    
    /**
     * Creates the output_files directory if it doesn't exist.
     */
    public static void ensureOutputDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(OUTPUT_DIR));
        } catch (IOException e) {
            System.err.println("Warning: Could not create output directory: " + e.getMessage());
        }
    }
}
