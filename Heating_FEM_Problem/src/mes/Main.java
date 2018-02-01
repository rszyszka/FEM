/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import Jama.Matrix;
import calka.Calka;
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
        
        System.out.println(gd.getDtau());
        int count = 0;
        //grid.makeFileVTK(count++);

        for (double itau = 0; itau < gd.getTau(); itau += gd.getDtau()) {
            gd.compute();
            Matrix t = gd.getH_global().solve(gd.getP_global());

            for (int i = 0; i < gd.getNh(); i++) {
                grid.ND[i].setT(t.getArray()[i][0]);
            }
            grid.usrednijTempWody();
            
            grid.wypiszTemp(510);
            //grid.makeFileVTK(count++);
        }
        System.out.println("\n\n");
        count = 0;
        for (int i = 0; i < gd.getnB(); i++) {
            for (int j = 0; j < gd.getnH(); j++) {
                System.out.printf("%.15f\t", gd.getH_global().getArray()[i][j]);
            }
            System.out.println("");
        }
        System.out.println("\n\n");

        System.out.println("\n\n");
        count = 0;
        for (int i = 0; i < gd.getnB(); i++) {
            for (int j = 0; j < gd.getnH(); j++) {
                System.out.printf("%.15f\t", grid.ND[count++].getT());
            }
            System.out.println("");
        }
        System.out.println("\n\n");
        System.out.println(gd.getDtau());

    }

}
