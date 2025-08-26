package sportbet.io;

import sportbet.model.RawMarket;

import java.nio.file.Path;
import java.util.List;

/**
 * Reads a JSON file containing an array of markets and returns a List<RawMarket>.
 * Use this when you are OK loading the whole file into memory.
 */
public interface JsonMarketListReader {
    List<RawMarket> read(Path file);
}
