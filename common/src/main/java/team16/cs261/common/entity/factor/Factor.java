package team16.cs261.common.entity.factor;

import com.mysema.query.annotations.QueryProjection;
import team16.cs261.common.meta.FactorClass;

/**
 * Created by martin on 24/01/15.
 */
public class Factor {

    private int id;

    private long tick;

    private FactorClass factor;

    private int edge;

    private int value;


    private double centile;
    private double sig;

    public Factor() {

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
}
