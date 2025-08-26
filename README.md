# 🎯 SportBet Market Conversion System

מערכת המרה לשווקי הימורי ספורט - ממירה נתוני שווקים גולמיים לפורמט מובנה עם UIDs ייחודיים.

## 🚀 איך להריץ את הפרויקט

### דרישות מערכת
- Java 17+
- Maven 3.6+

### שלבי הרצה

#### 1. שכפול הפרויקט
```bash
git clone <repository-url>
cd sport_betting_market_conversion_exercise
```

#### 2. בנייה
```bash
mvn clean package
```

#### 3. הרצת המערכת
```bash
# המרה עם קובץ פלט מותאם אישית
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar <input-file> <output-file>

# דוגמה
java -jar target/market-conversion-0.1.0-SNAPSHOT.jar src/test/resources/samples/market_input_example.json output.json
```

#### 4. הרצת טסטים
```bash
mvn test
```

## � פורמט קלט ופלט

### קובץ קלט (JSON)
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

### קובץ פלט (JSON)
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

## � סוגי שווקים נתמכים

- **1x2** (Market Type 1)
- **Total** (Market Type 18) - עם specifier
- **1st Half Total** (Market Type 68) - עם specifier  
- **Handicap** (Market Type 16) - עם specifier
- **1st Half Handicap** (Market Type 66) - עם specifier
- **2nd Half Handicap** (Market Type 88) - עם specifier
- **Both Teams to Score** (Market Type 50)

---

**הפרויקט מוכן לשימוש!** 🎉
