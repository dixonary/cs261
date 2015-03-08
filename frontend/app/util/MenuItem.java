package util;

import play.api.mvc.Call;

import java.util.List;

/**
 * Created by martin on 08/03/15.
 */
public interface MenuItem {

    Directory getParent();

    String getLabel();

    String getIcon();

    Call getCall();

    boolean isRoot();

    boolean isActive(String uri);

    boolean isLive(String uri);

    boolean hasChildren();

    List<MenuItem> getChildren();

}
