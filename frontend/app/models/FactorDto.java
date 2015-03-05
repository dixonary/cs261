package models;

import com.mysema.query.annotations.QueryProjection;
import team16.cs261.common.meta.FactorClass;

/**
 * Created by martin on 04/03/15.
 */
public class FactorDto {

    private int id;
    private int tick;
    private long time;

    private FactorClass factor;
    private String factorLabel;

    //private int edge;
    private int value;
    private double centile;
    private double sig;

    @QueryProjection
    public FactorDto(int id, int tick, long time, String factor, int value, double centile, double sig) {
        this.id = id;
        this.tick = tick;
        this.time = time;
        this.factor = FactorClass.valueOf(factor);
        this.factorLabel = this.factor.getLabel();
        this.value = value;
        this.sig = sig;
        this.centile = centile;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFactorLabel() {
        return factorLabel;
    }

    public void setFactorLabel(String factorLabel) {
        this.factorLabel = factorLabel;
    }

    public double getCentile() {
        return centile;
    }

    public void setCentile(double centile) {
        this.centile = centile;
    }

    public FactorClass getFactor() {
        return factor;
    }

    public void setFactor(FactorClass factor) {
        this.factor = factor;
    }

    public double getSig() {
        return sig;
    }

    public void setSig(double sig) {
        this.sig = sig;
    }
}
