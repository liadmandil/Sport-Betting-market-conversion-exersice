package sportbet.model;
import java.util.List;

// Raw market data from input JSON
public class RawMarket {
    private String name;
    private String event_id;
    private List<RawSelection> selections;

    public RawMarket() {}

    public RawMarket(String name, String event_id, List<RawSelection> selections) {
        this.name = name;
        this.event_id = event_id;
        this.selections = selections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public List<RawSelection> getSelections() {
        return selections;
    }

    public void setSelections(List<RawSelection> selections) {
        this.selections = selections;
    }

    @Override
    public String toString() {
        return "RawMarket{name='" + name + "', event_id='" + event_id + "', selections=" + selections + "}";
    }
}
