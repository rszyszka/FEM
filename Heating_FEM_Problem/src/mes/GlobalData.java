/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import Jama.Matrix;
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

    private final double H_wody, B_wody, B_scianki_stali, H_zeliwa;
    private final int nH_wody, nB_wody, nB_scianki_stali, nH_zeliwa;

    private final int nh; //liczba wezlow
    private final int ne; //liczba elementow

    private final double t_begin; //temperatura poczatkowa
    private final double tau; //czas procesu
    private double dtau; //poczatkowa wartosc przyrostu czasu
    private final double alfa; //wspolczynnik wymiany ciepla
    private final double c; //cieplo wlasciwe
    private final double k; //wspolczynnik przewodzenia ciepla
    private final double ro; //gestosc

    public final double alfaStal, alfaZeliwo;
    public final double cStal, cWoda, cZeliwo;
    public final double kStal, kWoda, kZeliwo;
    public final double roStal, roWoda, roZeliwo;
    public final double t_otoczenia_powietrze, t_otoczenia_ogien;

    private final LokalElement2P el_lok;
    private final double[][] H_current;
    private final double[] P_current;
    private final Matrix H_global;
    private final Matrix P_global;

    private static GlobalData globalData;

    private GlobalData() throws FileNotFoundException {

        Scanner input = new Scanner(new File("data.txt"));
        this.H_wody = input.nextDouble();
        input.findInLine(";");
        this.B_wody = input.nextDouble();
        input.findInLine(";");
        this.nH_wody = input.nextInt();
        input.findInLine(";");
        this.nB_wody = input.nextInt();
        input.findInLine(";");
        this.B_scianki_stali = input.nextDouble();
        input.findInLine(";");
        this.nB_scianki_stali = input.nextInt();
        input.findInLine(";");
        this.H_zeliwa = input.nextDouble();
        input.findInLine(";");
        this.nH_zeliwa = input.nextInt();
        input.findInLine(";");
        this.t_begin = input.nextDouble();
        input.findInLine(";");
        double tau1 = input.nextDouble();
        input.findInLine(";");
        this.t_otoczenia_ogien = input.nextDouble();
        input.findInLine(";");
        this.t_otoczenia_powietrze = input.nextDouble();
        input.findInLine(";");
        this.alfa = input.nextDouble();
        input.findInLine(";");
        this.cWoda = input.nextDouble();
        input.findInLine(";");
        this.kWoda = input.nextDouble();
        input.findInLine(";");
        this.roWoda = input.nextDouble();
        input.findInLine(";");
        this.cStal = input.nextDouble();
        input.findInLine(";");
        this.kStal = input.nextDouble();
        input.findInLine(";");
        this.roStal = input.nextDouble();
        input.findInLine(";");
        this.cZeliwo = input.nextDouble();
        input.findInLine(";");
        this.kZeliwo = input.nextDouble();
        input.findInLine(";");
        this.roZeliwo = input.nextDouble();
        input.close();

        this.B = 2 * B_scianki_stali + B_wody;
        this.H = H_zeliwa + H_wody + 2 * B_scianki_stali;
        this.nB = 2 * nB_scianki_stali + nB_wody - 2;
        this.nH = nH_zeliwa + nH_wody + 2 * nB_scianki_stali - 3;

//        kStal = 16.3;
//        kZeliwo = 56;
//        kWoda = 0.6;
//
//        roStal = 7900;
//        roZeliwo = 7250;
//        roWoda = 998;
//
//        cStal = 500;
//        cZeliwo = 850;
//        cWoda = 4190;
        k = (kZeliwo + kWoda + kStal) / 3.0;
        c = (cZeliwo + cWoda + cStal) / 3.0;
        ro = (roZeliwo + roWoda + roStal) / 3.0;

        double asr = k / (c * ro);
        this.dtau = Math.pow(B / nB, 2) / (0.5 * asr);

        int temp = (int) (tau1 / dtau) + 1;
        tau = dtau * temp;

        ne = (nH - 1) * (nB - 1);
        nh = nH * nB;

        el_lok = LokalElement2P.getInstance();
        H_current = new double[4][4];
        P_current = new double[4];
        H_global = new Matrix(nh, nh);
        P_global = new Matrix(nh, 1);

        alfaStal = alfa;
        alfaZeliwo = alfa;

    }

    public void compute() throws FileNotFoundException {

        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nh; j++) {
                H_global.set(i, j, 0);
            }
            P_global.set(i, 0, 0);
        }

        int typ_calkowania_S = 2;
        int typ_calkowania_V = (int) Math.pow(typ_calkowania_S, 2);

        double wages[] = {1, 1, 1, 1};

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

            for (int pc = 0; pc < typ_calkowania_V; pc++) { // 4 - liczba punktow calkowania po powierzchni w elemencie
                jakobian = new Jakobian(pc, x, y);
                t0p = 0;

                for (int j = 0; j < 4; j++) { // 4 - liczba wezlow w wykorzystywanym elemencie skonczonym
                    dNdx[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[0][0] * el_lok.getdN_dKsi()[pc][j]
                            + jakobian.getJ_inverted()[0][1] * el_lok.getdN_dEta()[pc][j]);

                    dNdy[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[1][0] * el_lok.getdN_dKsi()[pc][j]
                            + jakobian.getJ_inverted()[1][1] * el_lok.getdN_dEta()[pc][j]);

                    t0p += temp_0[j] * el_lok.getN()[pc][j];
                }
                detj = Math.abs(jakobian.getDet()) * wages[pc];

                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        cij = grid.EL[el_nr].getC() * grid.EL[el_nr].getRo() * el_lok.getN()[pc][i] * el_lok.getN()[pc][j] * detj;
                        H_current[i][j] += grid.EL[el_nr].getK() * (dNdx[i] * dNdx[j] + dNdy[i] * dNdy[j]) * detj + cij / dtau;
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

                for (int pc = 0; pc < typ_calkowania_S; pc++) {

                    detj = Math.abs(detj) * wages[pc];

                    for (int n = 0; n < 4; n++) {
                        for (int i = 0; i < 4; i++) {
                            H_current[n][i] += grid.EL[el_nr].getAlfa() * el_lok.getPOW()[id].N[pc][n] * el_lok.getPOW()[id].N[pc][i] * detj;
                        }
                        if (id == 1) {
                            P_current[n] += grid.EL[el_nr].getAlfa() * t_otoczenia_ogien * el_lok.getPOW()[id].N[pc][n] * detj;
                        } else {
                            P_current[n] += grid.EL[el_nr].getAlfa() * t_otoczenia_powietrze * el_lok.getPOW()[id].N[pc][n] * detj;
                        }
                    }
                }
            }
            //agregacja
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    //H_global[grid.EL[el_nr].globalNodeID[i]][grid.EL[el_nr].globalNodeID[j]] += H_current[i][j];
                    H_global.set(grid.EL[el_nr].globalNodeID[i], grid.EL[el_nr].globalNodeID[j], H_global.get(grid.EL[el_nr].globalNodeID[i], grid.EL[el_nr].globalNodeID[j]) + H_current[i][j]);
                }
                //P_global[grid.EL[el_nr].globalNodeID[i]] += P_current[i];
                P_global.set(grid.EL[el_nr].globalNodeID[i], 0, P_global.get(grid.EL[el_nr].globalNodeID[i], 0) + P_current[i]);
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

    public Matrix getH_global() {
        return H_global;
    }

    public Matrix getP_global() {
        return P_global;
    }

    public double getH_wody() {
        return H_wody;
    }

    public double getB_wody() {
        return B_wody;
    }

    public double getB_scianki_stali() {
        return B_scianki_stali;
    }

    public double getH_zeliwa() {
        return H_zeliwa;
    }

    public int getnH_wody() {
        return nH_wody;
    }

    public int getnB_wody() {
        return nB_wody;
    }

    public int getnB_scianki_stali() {
        return nB_scianki_stali;
    }

    public int getnH_zeliwa() {
        return nH_zeliwa;
    }

    public void setDtau(double dtau) {
        this.dtau = dtau;
    }
}
