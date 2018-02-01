/*
 * Copyright (C) 2017 Szysz
 */
package calka;

import mes.Jakobian;

/**
 *
 * @author Szysz
 */
public class Calka {

    private final double p2w[] = {1, 1};
    private final double p2wsp[] = {-0.577, 0.577};

    private final double p3w[] = {5.0 / 9.0, 8.0 / 9.0, 5.0 / 9.0};
    private final double p3wsp[] = {-0.7745, 0, 0.7745};

    private double f(double x, double y) {
        return x*x*y;
    }

    public double calka2P() {

        double calka = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                calka += f(p2wsp[i], p2wsp[j]) * p2w[i] * p2w[j];
            }
        }

        return calka;
    }

    public double calka2P(Jakobian[] jakobians, double x[], double y[]) {

        double calka = 0;
        double xGlobal[] = new double[4];
        double yGlobal[] = new double[4];

        for (int i = 0; i < 4; i++) {
            xGlobal[i] = 0;
            yGlobal[i] = 0;
            for (int j = 0; j < 4; j++) {
                xGlobal[i] += Jakobian.LE2P.getN()[i][j] * x[j];
                yGlobal[i] += Jakobian.LE2P.getN()[i][j] * y[j];
            }
        }

        for (int i = 0; i < 4; i++) {
            calka += f(xGlobal[i], yGlobal[i]) * jakobians[i].getDet();
        }

        return calka;
    }
    

    public double calka3P() {
        double calka = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                calka += f(p3wsp[i], p3wsp[j]) * p3w[i] * p3w[j];
            }
        }
        return calka;
    }

}
