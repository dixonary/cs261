package team16.cs261.common.entity.factor;

import team16.cs261.common.meta.FactorClass;

/**
 * Created by martin on 24/01/15.
 */
public class Factor {

    private int id;
    private int source;
    private int target;
    private FactorClass factor;
    private double score;


    private long tick;



    private int edge;

    private int value;


    private double centile;
    private double sig;

    public Factor() {

    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }

    public FactorClass getFactor() {
        return factor;
    }

    public void setFactor(FactorClass factor) {
        this.factor = factor;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getCentile() {
        return centile;
    }

    public void setCentile(double centile) {
        this.centile = centile;
    }

    public double getSig() {
        return sig;
    }

    public void setSig(double sig) {
        this.sig = sig;
    }

    @Override
    public String toString() {
        return "Factor{" +
                "score=" + score +
                ", factor=" + factor +
                ", target=" + target +
                ", source=" + source +
                ", id=" + id +
                '}';
    }
}
