package menu;

import play.api.mvc.Call;

/**
 * Created by martin on 14/02/15.
 */
public class MenuItem extends Item {

    final Call call;
    final Call[] children;

    public MenuItem(String icon, String label, Call call) {
        this(icon, label, call, new Call[0]);
    }

    public MenuItem(String icon, String label, Call call, Call[] children) {
        super(icon, label);
        this.call = call;
        this.children = children;
    }

    public Call getCall() {
        return call;
    }

    @Override
    public boolean isActive(String uri) {
        return uri.equals(call.url());
    }
}
