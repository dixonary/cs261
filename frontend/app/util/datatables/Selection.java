package util.datatables;

/**
 * Created by martin on 07/03/15.
 */
public class Selection {

    String id;
    String label;

    public Selection(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public Selection(Integer id, String label) {
        this.id = String.valueOf(id);
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
