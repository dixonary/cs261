package menu;

import play.api.mvc.Call;

/**
 * Created by martin on 14/02/15.
 */
public class MenuItem extends Item {

    Call call;

    public MenuItem(String label, Call call) {
        super(label);
        this.call = call;
    }

    public Call getCall() {
        return call;
    }

    @Override
    public boolean isActive(String uri) {
        return uri.equals(call.url());
    }
}
