package team16.cs261.common.entity.graph;

/**
 * Created by martin on 04/03/15.
 */
public class Edge {

    private int id;

    private int source;

    private int target;

    private double weight;

    public Edge() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                ", weight=" + weight +
                '}';
    }
}
