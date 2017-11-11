/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.FileNotFoundException;

/**
 *
 * @author Szysz
 */
public class Element {

    public Node ID[];

    public double dN_dKsi[][];
    public double dN_dEta[][];

    public Element(int i, int j, double db, double dh) throws FileNotFoundException {

        ID = new Node[4];

        ID[0] = new Node(i * db, j * dh);
        ID[1] = new Node((i + 1) * db, j * dh);
        ID[2] = new Node((i + 1) * db, (j + 1) * dh);
        ID[3] = new Node(i * db, (j + 1) * dh);

        dN_dKsi = new double[4][4];
        dN_dEta = new double[4][4];

        for (int k = 0; k < 4; k++) {
            dN_dKsi[k][0] = dN1dKsi(ID[k].getX(), ID[k].getY());
            dN_dKsi[k][1] = dN2dKsi(ID[k].getX(), ID[k].getY());
            dN_dKsi[k][2] = dN3dKsi(ID[k].getX(), ID[k].getY());
            dN_dKsi[k][3] = dN4dKsi(ID[k].getX(), ID[k].getY());

            dN_dEta[k][0] = dN1dEta(ID[k].getX(), ID[k].getY());
            dN_dEta[k][1] = dN2dEta(ID[k].getX(), ID[k].getY());
            dN_dEta[k][2] = dN3dEta(ID[k].getX(), ID[k].getY());
            dN_dEta[k][3] = dN4dEta(ID[k].getX(), ID[k].getY());

        }

    }

    private double dN1dKsi(double ksi, double eta) {
        return -0.25 * (1 - eta);
    }

    private double dN2dKsi(double ksi, double eta) {
        return 0.25 * (1 - eta);
    }

    private double dN3dKsi(double ksi, double eta) {
        return 0.25 * (1 + eta);
    }

    private double dN4dKsi(double ksi, double eta) {
        return -0.25 * (1 + eta);
    }

    private double dN1dEta(double ksi, double eta) {
        return -0.25 * (1 - ksi);
    }

    private double dN2dEta(double ksi, double eta) {
        return -0.25 * (1 + ksi);
    }

    private double dN3dEta(double ksi, double eta) {
        return 0.25 * (1 + ksi);
    }

    private double dN4dEta(double ksi, double eta) {
        return 0.25 * (1 - ksi);
    }

}
