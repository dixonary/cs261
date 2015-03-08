package models;

import com.mysema.query.annotations.QueryProjection;

/**
 * Created by martin on 08/03/15.
 */
public class Point {
    int x;
    double y1;
    int y2;

    @QueryProjection

    public Point(int x, double y1, int y2) {
        this.x = x;
        this.y1 = y1;
        this.y2 = y2;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
}
