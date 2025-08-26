package sportbet.model;

/**
 * RawSelection mirrors a single selection object exactly as it appears in the input JSON.
 * Contains the name of the selection and the decimal odds value.
 */
public class RawSelection {
    private String name;
    private Double odds;


    public RawSelection() {
        // default constructor
    }

    public RawSelection(String name, Double odds) {
        this.name = name;
        this.odds = odds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        return "RawSelection{name='" + name + "', odds=" + odds + "}";
    }
}
