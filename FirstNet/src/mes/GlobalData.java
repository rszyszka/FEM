/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import lokal.LokalElement2P;

/**
 *
 * @author Szysz
 */
public class GlobalData {

    private final double H, B; //wysokosc, szerokosc
    private final int nH, nB; //liczba wezlow po wysokosci i po szerokosci

    private final int nh; //liczba wezlow
    private final int ne; //liczba elementow

    private final double t_begin; //temperatura poczatkowa
    private final double tau; //czas procesu
    private double dtau; //poczatkowa wartosc przyrostu czasu
    private final double t_otoczenia; //temperatura otoczenia
    private final double alfa; //wspolczynnik wymiany ciepla
    private final double c; //cieplo wlasciwe
    private final double k; //wspolczynnik przewodzenia ciepla
    private final double ro; //gestosc

    private final LokalElement2P el_lok;
    private final double[][] H_current;
    private final double[] P_current;
    private final double[][] H_global;
    private final double[] P_global;

    private static GlobalData globalData;

    private GlobalData() throws FileNotFoundException {

        Scanner input = new Scanner(new File("data.txt"));
        input.hasNextDouble();
        this.H = input.nextDouble();
        input.findInLine(";");
        this.B = input.nextDouble();
        input.findInLine(";");
        this.nH = input.nextInt();
        input.findInLine(";");
        this.nB = input.nextInt();
        input.findInLine(";");
        this.t_begin = input.nextDouble();
        input.findInLine(";");
        this.tau = input.nextDouble();
        input.findInLine(";");
        this.dtau = input.nextDouble();
        input.findInLine(";");
        this.t_otoczenia = input.nextDouble();
        input.findInLine(";");
        this.alfa = input.nextDouble();
        input.findInLine(";");
        this.c = input.nextDouble();
        input.findInLine(";");
        this.k = input.nextDouble();
        input.findInLine(";");
        this.ro = input.nextDouble();
        input.close();

        ne = (nH - 1) * (nB - 1);
        nh = nH * nB;

        el_lok = LokalElement2P.getInstance();
        H_current = new double[4][4];
        P_current = new double[4];
        H_global = new double[nh][nh];
        P_global = new double[nh];

    }

    public void compute() throws FileNotFoundException {

        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nh; j++) {
                H_global[i][j] = 0;
            }
            P_global[i] = 0;
        }

        Grid grid = Grid.getInstance();
        Jakobian jakobian;
        double[] dNdx = new double[4];
        double[] dNdy = new double[4];
        double[] x = new double[4];
        double[] y = new double[4];
        double[] temp_0 = new double[4];
        double t0p, cij;
        int id;
        double detj = 0;

        for (int el_nr = 0; el_nr < ne; el_nr++) {

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    H_current[i][j] = 0;
                }
                P_current[i] = 0;
            }

            for (int i = 0; i < 4; i++) {
                id = grid.EL[el_nr].globalNodeID[i];
                x[i] = grid.ND[id].getX();
                y[i] = grid.ND[id].getY();
                temp_0[i] = grid.ND[id].getT();
            }

            for (int pc = 0; pc < 4; pc++) { // 4 - liczba punktow calkowania po powierzchni w elemencie
                jakobian = new Jakobian(pc, x, y);
                t0p = 0;

                for (int j = 0; j < 4; j++) { // 4 - liczba wezlow w wykorzystywanym elemencie skonczonym
                    dNdx[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[0][0] * el_lok.getdN_dKsi()[pc][j]
                            + jakobian.getJ_inverted()[0][1] * el_lok.getdN_dEta()[pc][j]);

                    dNdy[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[1][0] * el_lok.getdN_dKsi()[pc][j]
                            + jakobian.getJ_inverted()[1][1] * el_lok.getdN_dEta()[pc][j]);

                    t0p += temp_0[j] * el_lok.getN()[pc][j];
                }
                detj = Math.abs(jakobian.getDet()) ;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        cij = c * ro * el_lok.getN()[pc][i] * el_lok.getN()[pc][j] * detj;
                        H_current[i][j] += k * (dNdx[i] * dNdx[j] + dNdy[i] * dNdy[j]) * detj + cij / dtau;
                        P_current[i] += cij / dtau * t0p;
                    }
                }
            }
            //warunki brzegowe
            for (int ipow = 0; ipow < grid.EL[el_nr].getN_pow(); ipow++) {
                id = grid.EL[el_nr].getA_pow()[ipow];
                switch (id) {
                    case 0:
                        detj = Math.sqrt(Math.pow(grid.EL[el_nr].ND[3].getX() - grid.EL[el_nr].ND[0].getX(), 2)
                                + Math.pow(grid.EL[el_nr].ND[3].getY() - grid.EL[el_nr].ND[0].getY(), 2)) / 2.0;
                        break;
                    case 1:
                        detj = Math.sqrt(Math.pow(grid.EL[el_nr].ND[0].getX() - grid.EL[el_nr].ND[1].getX(), 2)
                                + Math.pow(grid.EL[el_nr].ND[0].getY() - grid.EL[el_nr].ND[1].getY(), 2)) / 2.0;
                        break;
                    case 2:
                        detj = Math.sqrt(Math.pow(grid.EL[el_nr].ND[1].getX() - grid.EL[el_nr].ND[2].getX(), 2)
                                + Math.pow(grid.EL[el_nr].ND[1].getY() - grid.EL[el_nr].ND[2].getY(), 2)) / 2.0;
                        break;
                    case 3:
                        detj = Math.sqrt(Math.pow(grid.EL[el_nr].ND[2].getX() - grid.EL[el_nr].ND[3].getX(), 2)
                                + Math.pow(grid.EL[el_nr].ND[2].getY() - grid.EL[el_nr].ND[3].getY(), 2)) / 2.0;
                        break;
                }

                for (int p = 0; p < 2; p++) {
                    for (int n = 0; n < 4; n++) {
                        for (int i = 0; i < 4; i++) {
                            H_current[n][i] += alfa * el_lok.getPOW()[id].N[p][n] * el_lok.getPOW()[id].N[p][i] * detj;
                        }
                        P_current[n] += alfa * t_otoczenia * el_lok.getPOW()[id].N[p][n] * detj;
                    }
                }
            }
            //agregacja
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    H_global[grid.EL[el_nr].globalNodeID[i]][grid.EL[el_nr].globalNodeID[j]] += H_current[i][j];
                }
                P_global[grid.EL[el_nr].globalNodeID[i]] += P_current[i];
            }
        }
    }

    public static GlobalData getInstance() throws FileNotFoundException {
        if (globalData == null) {
            globalData = new GlobalData();
        }
        return globalData;
    }

    public double getH() {
        return H;
    }

    public double getB() {
        return B;
    }

    public int getnH() {
        return nH;
    }

    public int getnB() {
        return nB;
    }

    public int getNh() {
        return nh;
    }

    public int getNe() {
        return ne;
    }

    public double getT_begin() {
        return t_begin;
    }

    public double getTau() {
        return tau;
    }

    public double getDtau() {
        return dtau;
    }

    public double getT_otoczenia() {
        return t_otoczenia;
    }

    public double getAlfa() {
        return alfa;
    }

    public double getC() {
        return c;
    }

    public double getK() {
        return k;
    }

    public double getRo() {
        return ro;
    }

    public LokalElement2P getEl_lok() {
        return el_lok;
    }

    public double[][] getH_current() {
        return H_current;
    }

    public double[] getP_current() {
        return P_current;
    }

    public double[][] getH_global() {
        return H_global;
    }

    public double[] getP_global() {
        return P_global;
    }

    public void setDtau(double dtau) {
        this.dtau = dtau;
    }
}
