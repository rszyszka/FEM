/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.FileNotFoundException;

/**
 *
 * @author Szysz
 */
public class Node {

    private final double x, y;
    private double t;
    private final int status;

    private final GlobalData GD;

    public Node(double x, double y) throws FileNotFoundException {
        GD = new GlobalData();

        this.x = x;
        this.y = y;

        if (this.x == 0.0 || this.y == 0.0 || this.x >= GD.getB() || this.y >= GD.getH()) {
            this.status = 1;
        } else {
            this.status = 0;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getT() {
        return t;
    }

    public int getStatus() {
        return status;
    }

    public void setT(double t) {
        this.t = t;
    }

}
