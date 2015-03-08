package models;

import com.mysema.query.annotations.QueryProjection;
import models.graph.EdgeDto;
import models.graph.NodeDto;
import team16.cs261.common.meta.FactorClass;

/**
 * Created by martin on 04/03/15.
 */
public class FactorDto {

    private int id;
    private int tick;
    private long time;

    private FactorClassDto factor;

    private EdgeDto edge;
    private NodeDto source;
    private NodeDto target;

    private int value;
    private double centile;
    private double sig;


    @QueryProjection
    public FactorDto(int id, int tick, long time, FactorClassDto factor, EdgeDto edge, NodeDto source, NodeDto target, int value, double centile, double sig) {
        this.id = id;
        this.tick = tick;
        this.time = time;

        this.factor = factor;
        this.edge = edge;

        this.source=source;
        this.target=target;

        this.value = value;
        this.sig = sig;
        this.centile = centile;
    }

    public EdgeDto getEdge() {
        return edge;
    }

    public void setEdge(EdgeDto edge) {
        this.edge = edge;
    }

    public NodeDto getSource() {
        return source;
    }

    public void setSource(NodeDto source) {
        this.source = source;
    }

    public NodeDto getTarget() {
        return target;
    }

    public void setTarget(NodeDto target) {
        this.target = target;
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

    public FactorClassDto getFactor() {
        return factor;
    }

    public void setFactor(FactorClassDto factor) {
        this.factor = factor;
    }
}
