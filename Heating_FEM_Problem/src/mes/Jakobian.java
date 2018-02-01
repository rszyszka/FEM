/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import lokal.LokalElement2P;

/**
 *
 * @author Szysz
 */
public class Jakobian {

    private final double J[][];
    private final double J_inverted[][];
    private final double det;

    private final int punktCalkowania; // 0 | 1 | 2 | 3
    public static final LokalElement2P LE2P = LokalElement2P.getInstance();

    public Jakobian(int pktCalkowania, double x[], double y[]) {

        this.punktCalkowania = pktCalkowania;

        J = new double[2][2];
        J[0][0] = LE2P.getdN_dKsi()[punktCalkowania][0] * x[0] + LE2P.getdN_dKsi()[punktCalkowania][1] * x[1] + LE2P.getdN_dKsi()[punktCalkowania][2] * x[2] + LE2P.getdN_dKsi()[punktCalkowania][3] * x[3];
        J[0][1] = LE2P.getdN_dKsi()[punktCalkowania][0] * y[0] + LE2P.getdN_dKsi()[punktCalkowania][1] * y[1] + LE2P.getdN_dKsi()[punktCalkowania][2] * y[2] + LE2P.getdN_dKsi()[punktCalkowania][3] * y[3];
        J[1][0] = LE2P.getdN_dEta()[punktCalkowania][0] * x[0] + LE2P.getdN_dEta()[punktCalkowania][1] * x[1] + LE2P.getdN_dEta()[punktCalkowania][2] * x[2] + LE2P.getdN_dEta()[punktCalkowania][3] * x[3];
        J[1][1] = LE2P.getdN_dEta()[punktCalkowania][0] * y[0] + LE2P.getdN_dEta()[punktCalkowania][1] * y[1] + LE2P.getdN_dEta()[punktCalkowania][2] * y[2] + LE2P.getdN_dEta()[punktCalkowania][3] * y[3];

        det = J[0][0] * J[1][1] - J[0][1] * J[1][0];

        J_inverted = new double[2][2];

        J_inverted[0][0] = J[1][1];
        J_inverted[0][1] = -J[0][1];
        J_inverted[1][0] = -J[1][0];
        J_inverted[1][1] = J[0][0];
    }

    public Jakobian(int pktCalkowania, Element el) {

        this.punktCalkowania = pktCalkowania;

        J = new double[2][2];
        J[0][0] = LE2P.getdN_dKsi()[punktCalkowania][0] * el.ND[0].getX() + LE2P.getdN_dKsi()[punktCalkowania][1] * el.ND[1].getX() + LE2P.getdN_dKsi()[punktCalkowania][2] * el.ND[2].getX() + LE2P.getdN_dKsi()[punktCalkowania][3] * el.ND[3].getX();
        J[0][1] = LE2P.getdN_dKsi()[punktCalkowania][0] * el.ND[0].getY() + LE2P.getdN_dKsi()[punktCalkowania][1] * el.ND[1].getY() + LE2P.getdN_dKsi()[punktCalkowania][2] * el.ND[2].getY() + LE2P.getdN_dKsi()[punktCalkowania][3] * el.ND[3].getY();
        J[1][0] = LE2P.getdN_dEta()[punktCalkowania][0] * el.ND[0].getX() + LE2P.getdN_dEta()[punktCalkowania][1] * el.ND[1].getX() + LE2P.getdN_dEta()[punktCalkowania][2] * el.ND[2].getX() + LE2P.getdN_dEta()[punktCalkowania][3] * el.ND[3].getX();
        J[1][1] = LE2P.getdN_dEta()[punktCalkowania][0] * el.ND[0].getY() + LE2P.getdN_dEta()[punktCalkowania][1] * el.ND[1].getY() + LE2P.getdN_dEta()[punktCalkowania][2] * el.ND[2].getY() + LE2P.getdN_dEta()[punktCalkowania][3] * el.ND[3].getY();

        det = J[0][0] * J[1][1] - J[0][1] * J[1][0];

        J_inverted = new double[2][2];

        J_inverted[0][0] = J[1][1];
        J_inverted[0][1] = -J[0][1];
        J_inverted[1][0] = -J[1][0];
        J_inverted[1][1] = J[0][0];
    }

    public void wypiszJakobian() {
        System.out.println("Jakobian punktu calkowania id:" + punktCalkowania);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(+J[i][j] + "\t");
            }
            System.out.println("");
        }
        System.out.println("Det: " + det + "\n");
    }

    public double[][] getJ() {
        return J;
    }

    public double getDet() {
        return det;
    }

    public LokalElement2P getEl() {
        return LE2P;
    }

    public double[][] getJ_inverted() {
        return J_inverted;
    }
}
