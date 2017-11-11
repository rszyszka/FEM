/*
 * Copyright (C) 2017 Szysz
 */
package mes;

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

        Grid grid = new Grid();
        GlobalData gd = new GlobalData();
//         grid.wypiszND();
//        grid.wypiszEL(0);
//        System.out.println("\n");
//        grid.wypiszEL(gd.getnH() - 2);
//        System.out.println("\n");
//        grid.wypiszEL(gd.getNe() - (gd.getnH() - 1));
//        System.out.println("\n");
//        grid.wypiszEL(gd.getNe() - 1);

        double x[] = {-2, 1, 1, -1};
        double y[] = {-1, -1, 1, 1};

        double jak[][] = jakobian(grid.EL[0], x, y);

        wypiszJakobian(jak);

    }

    public static double[][] jakobian(Element el, double x[], double y[]) {

        double[][] J = new double[2][2];
        J[0][0] = el.dN_dKsi[0][0] * x[0] + el.dN_dKsi[0][1] * x[1] + el.dN_dKsi[0][2] * x[2] + el.dN_dKsi[0][3] * x[3];
        J[0][1] = el.dN_dKsi[0][0] * y[0] + el.dN_dKsi[0][1] * y[1] + el.dN_dKsi[0][2] * y[2] + el.dN_dKsi[0][3] * y[3];
        J[1][0] = el.dN_dEta[0][0] * x[0] + el.dN_dEta[0][1] * x[1] + el.dN_dEta[0][2] * x[2] + el.dN_dEta[0][3] * x[3];
        J[1][1] = el.dN_dEta[0][0] * y[0] + el.dN_dEta[0][1] * y[1] + el.dN_dEta[0][2] * y[2] + el.dN_dEta[0][3] * y[3];

        return J;
    }

    public static void wypiszJakobian(double[][] J) {

        System.out.println("Jakobian:");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(+J[i][j] + "\t");
            }
            System.out.println("");
        }
    }

}
