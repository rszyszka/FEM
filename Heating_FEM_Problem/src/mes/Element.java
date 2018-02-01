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

    public Node[] ND; // wezly w elemencie
    public Surface POW[]; //powierzchnie elementu

    private int n_pow; // liczba powierzchni ktore kontaktuja sie z otoczeniem
    private final int[] a_pow; // lokalne numery powierzchni kontaktowych elementu

    public int[] globalNodeID;

    GlobalData GD;
    
    private double k, ro, c, alfa;

    public Element(int i, int j, Node[] nodes) throws FileNotFoundException { //db - szerokosc 1 elementu, dh - wysokosc 1 elementu

        ND = new Node[4];
        POW = new Surface[4];
        globalNodeID = new int[4];
        GD = GlobalData.getInstance();

        //wyznaczamy wspolrzedne wezlow w elemencie
        ND[0] = nodes[0];
        ND[1] = nodes[1];
        ND[2] = nodes[2];
        ND[3] = nodes[3];

        //wyznaczamy globalne id wezlow w elemencie
        globalNodeID[0] = GD.getnH() * i + j;
        globalNodeID[1] = GD.getnH() * (i + 1) + j;
        globalNodeID[2] = GD.getnH() * (i + 1) + (j + 1);
        globalNodeID[3] = GD.getnH() * i + (j + 1);

        //wezly na powierzchniach
        POW[0] = new Surface(ND[3], ND[0]);
        POW[1] = new Surface(ND[0], ND[1]);
        POW[2] = new Surface(ND[1], ND[2]);
        POW[3] = new Surface(ND[2], ND[3]);

        n_pow = 0;
        for (int k = 0; k < 4; k++) {
            if (POW[k].getNodes()[0].getStatus() == 1 && POW[k].getNodes()[1].getStatus() == 1) {
                n_pow++;
            }
        }
        a_pow = new int[n_pow];

        int counter = 0;
        for (int k = 0; k < 4; k++) {
            if (POW[k].getNodes()[0].getStatus() == 1 && POW[k].getNodes()[1].getStatus() == 1) {
                a_pow[counter++] = k;
            }
        }
    }

    public int getN_pow() {
        return n_pow;
    }

    public int[] getA_pow() {
        return a_pow;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getRo() {
        return ro;
    }

    public void setRo(double ro) {
        this.ro = ro;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getAlfa() {
        return alfa;
    }

    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }
    
    
    
}
