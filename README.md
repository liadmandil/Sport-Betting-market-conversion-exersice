# 🎯 SportBet Market Conversion System

A system for converting sports betting markets - converts raw market data to structured format with unique UIDs.

## 🚀 How to Run the Project

### System Requirements
- Java 17+
- Maven 3.6+

### Running Steps

#### 1. Clone the Project
```bash
git clone https://github.com/liadmandil/Sport-Betting-market-conversion-exersice.git
cd Sport-Betting-market-conversion-exersice
```

#### 2. Build
```bash
mvn clean package
```


#### 3. Run the System
```bash
# Simple usage - just provide the filename (searches in INPUT_FILES directory)
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar <filename>

# Examples:
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar market_input_example.json
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar test_markets.json
```

**How it works:**
- **Input**: Place your JSON files in the `input_files/` directory
- **Automatic Resolution**: Just provide the filename, the system will find it automatically
- **Output**: Files are automatically saved in `output_files/` with `_output` suffix
- **Example**: `test_markets.json` → `test_markets_output.json`

#### 4. Run Tests
```bash
mvn test
```

## 📁 Directory Structure

The project now includes dedicated directories for input and output files:

```
project-root/
├── input_files/           # Place your JSON input files here
│   ├── market_input_example.json
│   └── test_markets.json
├── output_files/          # Converted files appear here automatically  
│   ├── market_input_example_output.json
│   └── test_markets_output.json
├── src/                   # Source code
└── target/                # Compiled files and JAR
```

## 📋 Input and Output Format

### Input File (JSON)
```json
[
  {
    "name": "1x2",
    "event_id": "123456",
    "selections": [
      {"name": "Team A", "odds": 1.65},
      {"name": "draw", "odds": 3.2},
      {"name": "Team B", "odds": 2.6}
    ]
  }
]
```

### Output File (JSON)
```json
[
  {
    "market_uid": "123456_1",
    "market_type_id": "1",
    "specifiers": {},
    "selections": [
      {
        "selection_uid": "123456_1_1",
        "selection_type_id": 1,
        "decimal_odds": 1.65
      }
    ]
  }
]
```

## 🏆 Supported Market Types

- **1x2** (Market Type 1)
- **Total** (Market Type 18) - with specifier
- **1st Half Total** (Market Type 68) - with specifier  
- **Handicap** (Market Type 16) - with specifier
- **1st Half Handicap** (Market Type 66) - with specifier
- **2nd Half Handicap** (Market Type 88) - with specifier
- **Both Teams to Score** (Market Type 50)

## 🏗️ Project Architecture & File Structure

### 📂 Package Structure

```
src/main/java/sportbet/
├── app/               # Application entry points
├── core/              # Core conversion logic
├── domain/            # Domain models and enums
├── errors/            # Error handling and exceptions
├── io/                # Input/Output operations
├── model/             # Data transfer objects
├── normalize/         # Data normalization utilities
├── uid/               # Unique identifier generation
└── validate/          # Data validation logic
```

### 📄 Detailed File Descriptions

#### 🚀 Application Layer (`sportbet.app`)

**Main.java**
- **Purpose**: Application entry point for command-line execution
- **Usage**: Handles command-line arguments, orchestrates the conversion process
- **Functionality**: Validates arguments, reads input file, performs conversion, writes output file

#### 🔧 Core Layer (`sportbet.core`)

**MarketConverter.java**
- **Purpose**: Main conversion engine that transforms raw markets to parsed markets
- **Usage**: Central component that coordinates all conversion logic
- **Functionality**: Market type identification, specifier extraction, UID generation, data transformation

#### 🎯 Domain Layer (`sportbet.domain`)

**MarketType.java**
- **Purpose**: Enum defining all supported market types with their mappings
- **Usage**: Provides market type identification and selection mapping logic
- **Functionality**: Maps market names to IDs, provides selection type mappings for each market type

**SpecifierType.java**
- **Purpose**: Enum for handling market specifiers (handicap, total values)
- **Usage**: Extracts and validates specifier values from market names
- **Functionality**: Parses decimal values from market names, validates specifier formats

#### ❌ Error Handling (`sportbet.errors`)

**DomainException.java**
- **Purpose**: Base exception class for all domain-specific errors
- **Usage**: Parent class for all custom exceptions in the system
- **Functionality**: Provides structured error handling with error codes

**ErrorCode.java**
- **Purpose**: Enum defining all possible error types in the system
- **Usage**: Standardizes error categorization across the application
- **Functionality**: Provides consistent error codes and messages

**MissingFileException.java**
- **Purpose**: Exception thrown when input files are not found
- **Usage**: File reading operations error handling
- **Functionality**: Specific exception for missing file scenarios

**JsonReadException.java**
- **Purpose**: Exception thrown when JSON parsing fails
- **Usage**: JSON processing error handling
- **Functionality**: Handles malformed JSON or parsing errors

**FileFormatException.java**
- **Purpose**: Exception thrown when file format is invalid
- **Usage**: File format validation error handling
- **Functionality**: Handles unsupported file formats or invalid structures

#### 📁 Input/Output Layer (`sportbet.io`)

**JsonMarketListReader.java**
- **Purpose**: Interface defining contract for reading market lists from files
- **Usage**: Abstraction for different file reading implementations
- **Functionality**: Provides standard interface for file reading operations

**JacksonListMarketReader.java**
- **Purpose**: Jackson-based implementation for reading JSON market files
- **Usage**: Concrete implementation for JSON file parsing
- **Functionality**: Uses Jackson library to parse JSON files into RawMarket objects

**FilePathResolver.java**
- **Purpose**: Utility for automatic input/output file path resolution
- **Usage**: Resolves file paths for input_files and output_files directories
- **Functionality**: Automatic file discovery, output file naming with suffix, directory management

#### 📊 Model Layer (`sportbet.model`)

**RawMarket.java**
- **Purpose**: Data model representing input market data structure
- **Usage**: Holds raw market data as received from input files
- **Functionality**: Contains market name, event ID, and raw selections

**RawSelection.java**
- **Purpose**: Data model representing input selection data structure
- **Usage**: Holds raw selection data within raw markets
- **Functionality**: Contains selection name and odds

**ParsedMarket.java**
- **Purpose**: Data model representing converted/output market data structure
- **Usage**: Holds processed market data ready for output
- **Functionality**: Contains market UID, type ID, specifiers, and parsed selections

**ParsedSelection.java**
- **Purpose**: Data model representing converted/output selection data structure
- **Usage**: Holds processed selection data within parsed markets
- **Functionality**: Contains selection UID, type ID, and decimal odds

#### 🔄 Normalization Layer (`sportbet.normalize`)

**MarketNormalizer.java**
- **Purpose**: Utility class for normalizing and processing market data
- **Usage**: Cleans and standardizes market names and data
- **Functionality**: Text normalization, data cleaning, format standardization

#### 🆔 UID Generation Layer (`sportbet.uid`)

**UidGenerator.java**
- **Purpose**: Centralized service for generating unique identifiers
- **Usage**: Creates consistent UIDs for markets and selections
- **Functionality**: Generates market UIDs (event_id_market_type_id) and selection UIDs (market_uid_selection_type_id)

**MarketUidParams.java**
- **Purpose**: Parameter object for market UID generation
- **Usage**: Type-safe parameter passing for market UID creation
- **Functionality**: Encapsulates event ID and market type ID

**SelectionUidParams.java**
- **Purpose**: Parameter object for selection UID generation
- **Usage**: Type-safe parameter passing for selection UID creation
- **Functionality**: Encapsulates market UID and selection type ID

#### ✅ Validation Layer (`sportbet.validate`)

**RawMarketValidator.java**
- **Purpose**: Validates input market data before processing
- **Usage**: Ensures raw market data meets required standards
- **Functionality**: Validates required fields, data formats, and business rules

**ParsedMarketValidator.java**
- **Purpose**: Validates converted market data before output
- **Usage**: Ensures parsed market data is correctly formatted
- **Functionality**: Validates UID formats, type IDs, and output structure

**ValidationParams.java**
- **Purpose**: Parameter object for validation operations
- **Usage**: Type-safe parameter passing for validation methods
- **Functionality**: Encapsulates validation context and parameters

## 🧪 Testing Structure

The project includes comprehensive JUnit 5 test coverage with 35 passing tests:

- **Unit Tests**: Each component has dedicated test classes
- **Integration Tests**: End-to-end conversion testing
- **Validation Tests**: Error handling and edge case testing
- **Parameter Tests**: Testing parameter object functionality

---

**The project is ready to use!** 🎉
