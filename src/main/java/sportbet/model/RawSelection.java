package sportbet.model;

// Raw selection data from input JSON
public class RawSelection {
    private String name;
    private Double odds;

    public RawSelection() {}

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
