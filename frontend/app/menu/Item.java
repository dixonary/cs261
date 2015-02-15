package menu;

/**
 * Created by martin on 14/02/15.
 */
public abstract class Item {

    private final String icon;
    private final String label;

    public Item(String icon, String label) {
        this.icon = icon;
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public abstract boolean isActive(String uri);
}
