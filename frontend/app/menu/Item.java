package menu;

/**
 * Created by martin on 14/02/15.
 */
public abstract class Item {

    private final String label;

    public Item(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public abstract boolean isActive(String uri);
}
