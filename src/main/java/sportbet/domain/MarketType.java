package sportbet.domain;

import java.util.*;

/**
 * MarketType is the domain "source of truth".
 * For each market it holds:
 *  - the internal market_type_id (as specified by the exercise)
 *  - the specifier kind (NONE / TOTAL / HCP)
 *  - a normalized selection-name -> selection_type_id map
 *  - a set of normalized aliases for resolving the market from its name
 *
 * It also provides helpers:
 *  - fromName(String) to resolve a MarketType by its display name
 *  - resolveSelectionTypeId(String) to map a selection label to its ID
 *
 * Notes:
 *  - Totals for full time (18) and 1st half (68) share the same selection IDs (Over=12, Under=13).
 *  - Handicaps for full time (16), 1st half (66), 2nd half (88) share the same selection IDs
 *    (Team A=1714, Team B=1715).
 *  - 1x2 (1) -> Team A=1, Draw=2, Team B=3.
 *  - BTTS (50) -> Yes=10, No=11.
 */
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

    /** Returns the internal market_type_id (e.g., "18" for Total). */
    public String getTypeId() {
        return typeId;
    }

    /** Returns the specifier kind this market expects (NONE / TOTAL / HCP). */
    public SpecifierType getSpecifierType() {
        return specifierType;
    }

    /**
     * Resolves a selection's textual label to its selection_type_id, if known.
     * The input is normalized (trim/lowercase and simple aliases like "o"/"u" -> "over"/"under").
     */
    public Optional<Integer> resolveSelectionTypeId(String selectionName) {
        String key = normalizeSelection(selectionName);
        return Optional.ofNullable(selectionMap.get(key));
    }

    /**
     * Resolves a MarketType from a market display name using normalized aliases.
     * Example: "Total" -> TOTAL, "1st half - handicap" -> FIRST_HALF_HANDICAP.
     */
    public static Optional<MarketType> fromName(String marketName) {
        if (marketName == null) return Optional.empty();
        String n = normalizeBasic(marketName);
        for (MarketType mt : values()) {
            if (mt.aliasSet.contains(n)) return Optional.of(mt);
        }
        return Optional.empty();
    }

    // ---- Normalization helpers ----

    private static Map<String, Integer> normalizeSelectionMap(Map<String, Integer> src) {
        Map<String, Integer> out = new HashMap<>();
        src.forEach((k, v) -> out.put(normalizeBasic(k), v));
        // also support short aliases for totals:
        // e.g., "o" -> "over", "u" -> "under"
        if (out.containsKey("over") && !out.containsKey("o")) out.put("o", out.get("over"));
        if (out.containsKey("under") && !out.containsKey("u")) out.put("u", out.get("under"));
        return out;
    }

    private static Set<String> normalizeAliases(List<String> aliases) {
        Set<String> s = new HashSet<>();
        for (String a : aliases) s.add(normalizeBasic(a));
        return s;
    }

    private static String normalizeSelection(String s) {
        String n = normalizeBasic(s);
        // map common short forms to canonical keys
        if (n.equals("o")) return "over";
        if (n.equals("u")) return "under";
        return n;
    }

    private static String normalizeBasic(String s) {
        return (s == null) ? "" : s.trim().toLowerCase();
    }
}
