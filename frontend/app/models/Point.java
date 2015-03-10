package models;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 08/03/15.
 */
public class Point {
    long x;
    double y1;
    double y2;

    @QueryProjection
    public Point(long x, double y1, double y2) {
        this.x = x;
        this.y1 = y1;
        this.y2 = y2;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }
}
