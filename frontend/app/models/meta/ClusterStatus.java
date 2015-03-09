package models.meta;

/**
 * Created by martin on 09/03/15.
 */
public enum ClusterStatus {
    UNSEEN("New"),
    SEEN("Pending"),
    INVESTIGATED("Investigated");

    public String label;

    ClusterStatus(String label) {
        this.label = label;
    }
}
