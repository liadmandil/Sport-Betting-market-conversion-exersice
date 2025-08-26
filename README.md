# 🎯 Sports Betting Market Conversion System

## 📋 What the System Does

This system converts raw sports betting market data into structured format with unique identifiers and standardized types.

### 🔄 Input → Output Process

**Input:** Raw JSON file containing betting markets on sports events
```json
{
  "name": "Total",
  "event_id": "123456",
  "selections": [
    {"name": "over 2.5", "odds": 1.85},
    {"name": "under 2.5", "odds": 1.95}
  ]
}
```

**Output:** Structured data with UIDs and standardized IDs
```json
{
  "market_uid": "123456_18_2.5",
  "market_type_id": "18",
  "specifiers": {"total": "2.5"},
  "selections": [
    {"selection_uid": "123456_18_2.5_12", "selection_type_id": "12", "decimal_odds": 1.85},
    {"selection_uid": "123456_18_2.5_13", "selection_type_id": "13", "decimal_odds": 1.95}
  ]
}
```

## 🎮 Core Functions

### 1. **Market Type Recognition**
- Identifies market types (1x2, Total, Handicap, BTTS, etc.)
- Maps market names to standardized type IDs

### 2. **UID Generation**
- **Market UID**: `{event_id}_{market_type_id}_{specifier}`
- **Selection UID**: `{market_uid}_{selection_type_id}`

### 3. **Selection Type Mapping**
Converts selection names to standardized IDs:
- `"Team A"` → `"1"`, `"Draw"` → `"2"`, `"Team B"` → `"3"`
- `"Over"` → `"12"`, `"Under"` → `"13"`
- `"Yes"` → `"10"`, `"No"` → `"11"`

### 4. **Specifier Extraction**
- **TOTAL**: Extracts values like "2.5" from "Over 2.5"
- **HCP (Handicap)**: Preserves direction "+1.5" or "-0.5" based on home team perspective

## � Key Convention: Home Team Handicap Direction

- **Positive (+1.5)**: Home team (Team A) receives advantage
- **Negative (-0.5)**: Home team (Team A) receives disadvantage

## 🚀 Usage

```bash
java -jar market-conversion.jar input_file.json
```

The system will:
1. Read the input file from `input_files/` directory
2. Convert all markets according to specifications
3. Save output to `output_files/` directory with `_output` suffix

## 📊 Supported Market Types

| Market | Type ID | Specifier | Example UID |
|--------|---------|-----------|-------------|
| 1x2 | 1 | None | `123456_1` |
| Total | 18 | Yes | `123456_18_2.5` |
| 1st Half Total | 68 | Yes | `123456_68_1.5` |
| Handicap | 16 | Yes | `123456_16_+1.5` |
| 1st Half Handicap | 66 | Yes | `123456_66_+0.5` |
| 2nd Half Handicap | 88 | Yes | `123456_88_+1` |
| Both Teams to Score | 50 | None | `123456_50` |

---

**The system is ready for production use!** ✅
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


#### 5. Run Tests
```bash
mvn test
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