package sportbet.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import sportbet.errors.DomainException;
import sportbet.errors.ErrorCode;
import sportbet.errors.MissingFileException;
import sportbet.model.RawMarket;

/**
  Jackson-based reader that deserializes the entire JSON array into List<RawMarket>.
**/

public class JacksonListMarketReader implements JsonMarketListReader {
    private final ObjectMapper mapper;

    public JacksonListMarketReader() {
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  // check about FAIL_ON_UNKNOWN_PROPERTIES
    }

    @Override
    public List<RawMarket> read(Path file) {
        if (!Files.exists(file) || !Files.isRegularFile(file) || !Files.isReadable(file)) {
            throw new MissingFileException("Input file not found / not a regular file / not readable: "
                    + file.toAbsolutePath());
        }
        
        try {
            List<RawMarket> list = mapper.readValue(
                    file.toFile(), new TypeReference<List<RawMarket>>() {});
            if (list == null) {
                throw new FileFormatException("JSON root is null or not an array: " + file.toAbsolutePath());
            }
            return list;
        } catch (IOException e) {
            throw new JsonReadException("Failed to read/parse JSON: " + file.toAbsolutePath(), e);
        } catch (DomainException de) {
            throw de; // already categorized
        } catch (Exception e) {
            throw new DomainException(
                    ErrorCode.JSON_PARSE_ERROR,
                    "Unexpected error while reading JSON: " + e.getMessage(), e);
        }
    }
}
