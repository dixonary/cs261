package util.datatables;

/**
 * Created by martin on 07/03/15.
 */
public class DomainValue<E> {

    E id;
    String label;

    public DomainValue(E id, String label) {
        this.id = id;
        this.label = label;
    }

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
