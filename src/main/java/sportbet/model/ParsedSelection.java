package sportbet.model;

/**
 * ParsedSelection is the normalized/internal representation of a single selection.
 * It contains the resolved selection_type_id, a deterministic selection_uid, and decimal_odds.
 */
public class ParsedSelection {
    private String selection_uid;
    private int selection_type_id;
    private double decimal_odds;

    public ParsedSelection() {}

    public ParsedSelection(String selection_uid, int selection_type_id, double decimal_odds) {
        this.selection_uid = selection_uid;
        this.selection_type_id = selection_type_id;
        this.decimal_odds = decimal_odds;
    }

    public String getSelection_uid() {
        return selection_uid;
    }

    public void setSelection_uid(String selection_uid) {
        this.selection_uid = selection_uid;
    }

    public int getSelection_type_id() {
        return selection_type_id;
    }

    public void setSelection_type_id(int selection_type_id) {
        this.selection_type_id = selection_type_id;
    }

    public double getDecimal_odds() {
        return decimal_odds;
    }

    public void setDecimal_odds(double decimal_odds) {
        this.decimal_odds = decimal_odds;
    }

    @Override
    public String toString() {
        return "ParsedSelection{selection_uid='" + selection_uid + "', selection_type_id=" + selection_type_id +
                ", decimal_odds=" + decimal_odds + "}";
    }
}
