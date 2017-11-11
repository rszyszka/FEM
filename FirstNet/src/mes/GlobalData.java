/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Szysz
 */
public class GlobalData {

    private final double H, B;
    private int nH, nB;

    private final int nh;
    private final int ne;

    public GlobalData() throws FileNotFoundException {

        Scanner input = new Scanner(new File("data.txt"));
        this.H = input.nextDouble();
        input.findInLine(";");
        this.B = input.nextDouble();
        input.findInLine(";");
        this.nH = input.nextInt();
        input.findInLine(";");
        this.nB = input.nextInt();

        ne = (nH - 1) * (nB - 1);
        nh = nH * nB;
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

}
