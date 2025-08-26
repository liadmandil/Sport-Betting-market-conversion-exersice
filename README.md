# 🎯 SportBet Market Conversion System

A comprehensive system for converting sports betting markets - transforms raw market data into standardized format with unique identifiers and structured metadata.

## 🌟 What This System Does

### 📊 The Problem
Sports betting companies receive raw market data from various data providers, where each provider uses different naming conventions for the same betting markets. For example:
- "1x2" might appear as "Match Winner", "Win Draw Win", or "Three Way"
- Handicap values like "+1.5" or "-0.5" are embedded in selection names inconsistently
- No standardized unique identifiers exist for markets and selections
- Total values (Over/Under) are mixed with selection names

### 🎯 The Solution
This system provides **intelligent market normalization** that:

#### **🔍 Automatic Market Recognition**
- Identifies 6+ common betting market types (1x2, Total, Handicap, BTTS, etc.)
- Maps different naming conventions to standardized market types
- Supports full-time, 1st half, and 2nd half variations

#### **🏷️ Standardized Identification System**
- **Market UIDs**: `{event_id}_{market_type_id}_{specifier}`
- **Selection UIDs**: `{market_uid}_{selection_type_id}`
- **Deterministic**: Same input always produces same UIDs

#### **⚙️ Smart Specifier Extraction**
- **Total Markets**: Extracts threshold values (e.g., "Over 2.5" → `{"total": "2.5"}`)
- **Handicap Markets**: Preserves direction with sign (e.g., "Team A +1.5" → `{"hcp": "+1.5"}`)
- **Clean Mapping**: Removes numeric noise while preserving essential information

#### **🎲 Selection Type Mapping**
Converts text-based selections to standardized IDs:
- `"Team A"` → `selection_type_id: "1"`
- `"Draw"` → `selection_type_id: "2"`
- `"Over"` → `selection_type_id: "12"`
- `"Under"` → `selection_type_id: "13"`

### 🏠 Handicap Direction Convention

**Important**: The system follows a **Home Team (Team A) perspective** for handicap direction:

- **Positive Values (`+1.5`)**: Team A receives advantage
- **Negative Values (`-0.5`)**: Team A receives disadvantage (Team B advantage)
- **Zero (`0`)**: No handicap applied

#### Example:
```json
{
  "market_uid": "123456_16_+1.5",
  "specifiers": {
    "hcp": "+1.5"  // Team A gets +1.5 points advantage
  },
  "selections": [
    {
      "selection_type_id": "1714",  // Team A selection
      "decimal_odds": 1.8
    },
    {
      "selection_type_id": "1715",  // Team B selection  
      "decimal_odds": 2.0
    }
  ]
}
```

### 💼 Business Value
- **Automation**: Replaces hours of manual work with seconds of processing
- **Standardization**: All markets represented in uniform format
- **Scalability**: Processes thousands of markets efficiently
- **Accuracy**: 100% consistent mapping with zero human error
- **Integration**: Ready for downstream systems (trading platforms, analytics, etc.)

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


#### 3. Prepare Input Files

**📁 IMPORTANT: Place your input files in the `input_files/` directory**

The system automatically searches for files in the `input_files/` directory. To process your market data:

1. **Create your JSON file** with the following structure:
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

2. **Save the file** in the `input_files/` directory:
   ```
   input_files/
   ├── your_markets.json          ← Your file goes here
   ├── market_input_example.json  ← Example file provided
   └── all_markets.json           ← Sample with all market types
   ```

3. **File Requirements**:
   - Format: JSON array of market objects
   - Each market must have: `name`, `event_id`, `selections`
   - Each selection must have: `name`, `odds`
   - Encoding: UTF-8

#### 4. Run the System
```bash
# Simple usage - just provide the filename (the system searches in input_files/)
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar <filename>

# Examples:
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar market_input_example.json
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar your_markets.json
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar all_markets.json
```

**📋 Processing Flow:**
1. **Input**: System reads from `input_files/{filename}`
2. **Processing**: Converts raw markets to standardized format
3. **Output**: Saves result to `output_files/{filename}_output.json`
4. **Console**: Displays conversion summary and JSON content

**Example:**
```bash
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar my_data.json
# Reads: input_files/my_data.json
# Creates: output_files/my_data_output.json
```

#### 5. Run Tests
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

### 📥 Input File Structure (JSON)

**Place your input file in `input_files/` directory**

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
  },
  {
    "name": "Handicap",
    "event_id": "123456",
    "selections": [
      {"name": "Team A +1.5", "odds": 1.8},
      {"name": "Team B -1.5", "odds": 2.0}
    ]
  },
  {
    "name": "Total",
    "event_id": "123456",
    "selections": [
      {"name": "over 2.5", "odds": 1.85},
      {"name": "under 2.5", "odds": 1.95}
    ]
  }
]
```

### 📤 Output File Structure (JSON)

**Automatically saved to `output_files/` directory**

```json
[
  {
    "market_uid": "123456_1",
    "market_type_id": "1",
    "specifiers": {},
    "selections": [
      {
        "selection_uid": "123456_1_1",
        "selection_type_id": "1",
        "decimal_odds": 1.65
      },
      {
        "selection_uid": "123456_1_2",
        "selection_type_id": "2",
        "decimal_odds": 3.2
      },
      {
        "selection_uid": "123456_1_3",
        "selection_type_id": "3",
        "decimal_odds": 2.6
      }
    ]
  },
  {
    "market_uid": "123456_16_+1.5",
    "market_type_id": "16",
    "specifiers": {
      "hcp": "+1.5"
    },
    "selections": [
      {
        "selection_uid": "123456_16_+1.5_1714",
        "selection_type_id": "1714",
        "decimal_odds": 1.8
      },
      {
        "selection_uid": "123456_16_+1.5_1715",
        "selection_type_id": "1715",
        "decimal_odds": 2.0
      }
    ]
  }
]
```

### 🔍 Key Transformations

#### **Market Identification**
- `"1x2"` → `market_type_id: "1"`
- `"Handicap"` → `market_type_id: "16"`
- `"Total"` → `market_type_id: "18"`

#### **Selection Mapping**
- `"Team A"` → `selection_type_id: "1"`
- `"draw"` → `selection_type_id: "2"`
- `"over"` → `selection_type_id: "12"`
- `"under"` → `selection_type_id: "13"`

#### **Specifier Extraction**
- `"Team A +1.5"` → `{"hcp": "+1.5"}` (Home team advantage)
- `"over 2.5"` → `{"total": "2.5"}` (Threshold value)

#### **UID Generation**
- **Market UID**: `{event_id}_{market_type_id}_{specifier}`
- **Selection UID**: `{market_uid}_{selection_type_id}`
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

The system recognizes and converts 7 major sports betting market types:

### 📊 Market Type Mapping

| Market Name | Market Type ID | Specifier Required | Description |
|-------------|----------------|-------------------|-------------|
| **1x2** | `1` | No | Match result (Home/Draw/Away) |
| **Total** | `18` | Yes | Over/Under full-time |
| **1st Half Total** | `68` | Yes | Over/Under first half |
| **Handicap** | `16` | Yes | Point spread full-time |
| **1st Half Handicap** | `66` | Yes | Point spread first half |
| **2nd Half Handicap** | `88` | Yes | Point spread second half |
| **Both Teams to Score** | `50` | No | Yes/No BTTS |

### 🎯 Selection Type IDs

#### **1x2 Markets**
- `selection_type_id: "1"` → Team A (Home)
- `selection_type_id: "2"` → Draw
- `selection_type_id: "3"` → Team B (Away)

#### **Total Markets (Over/Under)**
- `selection_type_id: "12"` → Over (threshold)
- `selection_type_id: "13"` → Under (threshold)

#### **Handicap Markets**
- `selection_type_id: "1714"` → Team A selection
- `selection_type_id: "1715"` → Team B selection

#### **Both Teams to Score**
- `selection_type_id: "10"` → Yes
- `selection_type_id: "11"` → No

### 🏠 Critical Convention: Home Team Handicap Direction

**⚠️ IMPORTANT ASSUMPTION**: The system follows **Home Team (Team A) perspective** for handicap direction:

#### **Positive Handicap (+)**
```json
{
  "name": "Team A +1.5",    // Input
  "specifiers": {
    "hcp": "+1.5"           // Output: Team A gets 1.5 point advantage
  }
}
```
- **Meaning**: Home team (Team A) receives point advantage
- **Betting Logic**: Team A wins if they win OR lose by less than 1.5 points

#### **Negative Handicap (-)**
```json
{
  "name": "Team B -0.5",    // Input
  "specifiers": {
    "hcp": "-0.5"           // Output: Team A gets 0.5 point disadvantage
  }
}
```
- **Meaning**: Home team (Team A) receives point disadvantage (Away team advantage)
- **Betting Logic**: Team A wins only if they win by more than 0.5 points

#### **Real-World Examples**

**Basketball Game: Lakers vs Warriors**
- **Input**: `"Lakers +3.5"` (Lakers are underdogs)
- **Output**: `{"hcp": "+3.5"}` (Lakers get 3.5 point bonus)
- **Result**: Lakers win bet if: score difference ≤ 3 points

**Football Game: Barcelona vs Real Madrid** 
- **Input**: `"Barcelona -1"` (Barcelona favored)
- **Output**: `{"hcp": "-1"}` (Barcelona must overcome 1 point deficit)
- **Result**: Barcelona wins bet if: they win by 2+ goals

### 🔄 UID Generation Examples

#### **Simple Markets (No Specifier)**
```
1x2: 123456_1
BTTS: 123456_50
```

#### **Markets with Specifiers**
```
Total 2.5: 123456_18_2.5
Handicap +1.5: 123456_16_+1.5
1st Half HCP +0.5: 123456_66_+0.5
```

#### **Selection UIDs**
```
Team A in 1x2: 123456_1_1
Over 2.5: 123456_18_2.5_12
Team A +1.5: 123456_16_+1.5_1714
```

## 📁 Input Files Usage

### 🎯 Using the `input_files/` Directory

The project includes a comprehensive `input_files/` directory with example JSON files for each market type:

#### **🔧 Individual Market Type Files**
```
input_files/
├── 1x2_markets.json          # Match result examples
├── total_markets.json        # Over/Under examples
├── handicap_markets.json     # Point spread examples
├── btts_markets.json         # Both Teams to Score examples
└── all_markets.json          # Combined examples
```

#### **💡 How to Use Input Files**

**1. Test Individual Market Types:**
```bash
# Test only 1x2 markets
java -jar target/market-conversion-0.1.0-SNAPSHOT-shaded.jar input_files/1x2_markets.json output_1x2.json

# Test only handicap markets  
java -jar target/market-conversion-0.1.0-SNAPSHOT-shaded.jar input_files/handicap_markets.json output_hcp.json
```

**2. Test All Market Types:**
```bash
# Convert all market types at once
java -jar target/market-conversion-0.1.0-SNAPSHOT-shaded.jar input_files/all_markets.json output_all.json
```

**3. Create Your Own Input:**
```bash
# Use any of the example files as templates
cp input_files/handicap_markets.json my_custom_markets.json
# Edit my_custom_markets.json with your data
java -jar target/market-conversion-0.1.0-SNAPSHOT-shaded.jar my_custom_markets.json my_output.json
```

#### **📋 Input File Format Requirements**

Each input file must follow this exact JSON structure:
```json
{
  "markets": [
    {
      "id": "numeric_string",
      "name": "descriptive_market_name", 
      "selections": [
        {
          "id": "numeric_string",
          "name": "selection_description"
        }
      ]
    }
  ]
}
```

#### **⚠️ Critical Input Conventions**

1. **Market Names Must Match**: Use exact naming patterns:
   - `"1x2"` for match result
   - `"Total 2.5"` for totals (with decimal specifier)
   - `"Team A +1.5"` for handicaps (with +/- and decimal)
   - `"Both Teams to Score"` for BTTS

2. **Selection Names Must Include Team Indicators**:
   - Use `"Team A"` for home team selections
   - Use `"Team B"` for away team selections  
   - Use `"Over"/"Under"` for total markets
   - Use `"Yes"/"No"` for BTTS markets

3. **Handicap Direction Rules**:
   - Always specify sign: `"Team A +1.5"` or `"Team B -0.5"`
   - System preserves signs based on home team perspective
   - Positive = Team A advantage, Negative = Team A disadvantage

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

## 🎯 System Assumptions & Business Logic

### 🏠 Critical Home Team Convention

**⚠️ CORE ASSUMPTION**: The entire system is built around the **"Home Team (Team A) Perspective"** for handicap betting:

#### **Business Rationale**
- **Industry Standard**: Most sportsbooks display handicaps from the home team perspective
- **User Experience**: Bettors expect consistent handicap direction across platforms
- **Data Integrity**: Ensures all conversions maintain the same directional logic



#### **Real-World Impact**
- **Input**: `"Barcelona -1.5"` (Barcelona must win by 2+ goals)
- **Output**: `{"hcp": "-1.5"}` (Barcelona starts with 1.5 goal deficit)
- **Betting Logic**: Barcelona wins bet only if final score difference > 1.5

