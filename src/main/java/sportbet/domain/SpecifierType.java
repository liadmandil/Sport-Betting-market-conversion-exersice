package sportbet.domain;

/**
 * SpecifierType indicates which numeric parameter (if any) a market requires.
 * NONE  -> no numeric threshold (e.g., 1x2, BTTS)
 * TOTAL -> extract "total" from Over/Under selections (e.g., Over 2.5)
 * HCP   -> extract "hcp" (handicap) from selections (e.g., Team A +1.5)
 */
public enum SpecifierType {
    NONE, TOTAL, HCP
}
