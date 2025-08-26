package sportbet.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// Market types with their IDs, selection mappings and name aliases
public enum MarketType {

    ONE_X_TWO(
        "1",
        SpecifierType.NONE,
        Map.of("team a", 1, "draw", 2, "team b", 3),
        List.of("1x2")
    ),

    TOTAL(
        "18",
        SpecifierType.TOTAL,
        Map.of("over", 12, "under", 13),
        List.of("total")
    ),

    FIRST_HALF_TOTAL(
        "68",
        SpecifierType.TOTAL,
        Map.of("over", 12, "under", 13),
        List.of("1st half - total")
    ),

    HANDICAP(
        "16",
        SpecifierType.HCP,
        Map.of("team a", 1714, "team b", 1715),
        List.of("handicap")
    ),

    FIRST_HALF_HANDICAP(
        "66",
        SpecifierType.HCP,
        Map.of("team a", 1714, "team b", 1715),
        List.of("1st half - handicap")
    ),

    SECOND_HALF_HANDICAP(
        "88",
        SpecifierType.HCP,
        Map.of("team a", 1714, "team b", 1715),
        List.of("2nd half - handicap")
    ),

    BTTS(
        "50",
        SpecifierType.NONE,
        Map.of("yes", 10, "no", 11),
        List.of("both teams to score")
    );

    private final String typeId;
    private final SpecifierType specifierType;
    private final Map<String, Integer> selectionMap;   
    private final Set<String> aliasSet;                 

    MarketType(String typeId,
               SpecifierType specifierType,
               Map<String, Integer> rawSelectionMap,
               List<String> rawAliases) {
        this.typeId = typeId;
        this.specifierType = specifierType;
        this.selectionMap = Collections.unmodifiableMap(normalizeSelectionMap(rawSelectionMap));
        this.aliasSet = Collections.unmodifiableSet(normalizeAliases(rawAliases));
    }

    // Returns market type ID
    public String getTypeId() {
        return typeId;
    }

    // Returns specifier type (NONE/TOTAL/HCP)
    public SpecifierType getSpecifierType() {
        return specifierType;
    }

    // Maps selection name to selection type ID
    public Optional<Integer> resolveSelectionTypeId(String selectionName) {
        String key = normalizeSelection(selectionName);
        return Optional.ofNullable(selectionMap.get(key));
    }

    // Finds market type by exact name match
    public static Optional<MarketType> fromName(String marketName) {
        if (marketName == null) return Optional.empty();
        String n = normalizeBasic(marketName);
        for (MarketType mt : values()) {
            for (String alias : mt.aliasSet) {
                if (alias.equals(n)) return Optional.of(mt);
            }
        }
        return Optional.empty();
    }

    // Normalize selection map and add short aliases
    private static Map<String, Integer> normalizeSelectionMap(Map<String, Integer> src) {
        Map<String, Integer> out = new HashMap<>();
        src.forEach((k, v) -> out.put(normalizeBasic(k), v));
        if (out.containsKey("over") && !out.containsKey("o")) out.put("o", out.get("over"));
        if (out.containsKey("under") && !out.containsKey("u")) out.put("u", out.get("under"));
        return out;
    }

    // Normalize aliases to lowercase
    private static Set<String> normalizeAliases(List<String> aliases) {
        Set<String> s = new HashSet<>();
        for (String a : aliases) s.add(normalizeBasic(a));
        return s;
    }

    // Handle short aliases for selections
    private static String normalizeSelection(String s) {
        String n = normalizeBasic(s);
        if (n.equals("o")) return "over";
        if (n.equals("u")) return "under";
        return n;
    }

    // Basic normalization: trim and lowercase
    private static String normalizeBasic(String s) {
        return (s == null) ? "" : s.trim().toLowerCase();
    }
}
