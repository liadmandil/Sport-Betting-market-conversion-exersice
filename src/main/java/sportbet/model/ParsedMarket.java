package sportbet.model;

import java.util.List;
import java.util.Map;

// Parsed market with UID, type ID, specifiers and selections
public class ParsedMarket {
    private String market_uid;
    private String market_type_id;
    private Map<String, String> specifiers;
    private List<ParsedSelection> selections;

    public ParsedMarket() {}

    public ParsedMarket(String market_uid, String market_type_id,
                        Map<String, String> specifiers, List<ParsedSelection> selections) {
        this.market_uid = market_uid;
        this.market_type_id = market_type_id;
        this.specifiers = specifiers;
        this.selections = selections;
    }

    public String getMarket_uid() {
        return market_uid;
    }

    public void setMarket_uid(String market_uid) {
        this.market_uid = market_uid;
    }

    public String getMarket_type_id() {
        return market_type_id;
    }

    public void setMarket_type_id(String market_type_id) {
        this.market_type_id = market_type_id;
    }

    public Map<String, String> getSpecifiers() {
        return specifiers;
    }

    public void setSpecifiers(Map<String, String> specifiers) {
        this.specifiers = specifiers;
    }

    public List<ParsedSelection> getSelections() {
        return selections;
    }

    public void setSelections(List<ParsedSelection> selections) {
        this.selections = selections;
    }

    @Override
    public String toString() {
        return "ParsedMarket{market_uid='" + market_uid + "', market_type_id='" + market_type_id +
                "', specifiers=" + specifiers + ", selections=" + selections + "}";
    }
}
