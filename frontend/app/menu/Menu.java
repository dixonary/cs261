package menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 14/02/15.
 */
public class Menu extends Item {

    private final List<MenuItem> items = new ArrayList<>();

    public Menu(String label) {
        super(label);
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    @Override
    public boolean isActive(String uri) {
        for(MenuItem menuItem : items) {
            if(menuItem.isActive(uri)) {
                return true;
            }
        }
        return false;
    }
}
