package team16.cs261.common.entity.graph;

/**
 * Created by martin on 01/03/15.
 */
public class Node {

    private int id;

    public String label;

    public Node() {
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
