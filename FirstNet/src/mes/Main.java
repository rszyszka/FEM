/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import calka.UkladyRownanLiniowych;
import java.io.FileNotFoundException;

/**
 *
 * @author Szysz
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {

        GlobalData gd = GlobalData.getInstance();
        Grid grid = Grid.getInstance();
        double[] t;

        for (int itau = 0; itau < gd.getTau(); itau++) {
            gd.compute();
            t = UkladyRownanLiniowych.gaussElimination(gd.getNh(), gd.getH_global(), gd.getP_global());
            for (int i = 0; i < gd.getNh(); i++) {
                grid.ND[i].setT(t[i]);
            }
        }

        int count = 0;
        for (int i = 0; i < gd.getnB(); i++) {
            for (int j = 0; j < gd.getnH(); j++) {
                System.out.printf("%.15f\t", grid.ND[count++].getT());
            }
            System.out.println("");
        }
        System.out.println("\n\n");

    }

}
