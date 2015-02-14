package menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 14/02/15.
 */
public class Navigation {

    private final String label;

    private final List<Item> items = new ArrayList<>();

    public Navigation(String label) {
        this.label=label;
    }

    public String getLabel() {
        return label;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}
