/*
 * Copyright (C) 2017 Szysz
 */
package calka;

import java.util.Scanner;

/**
 *
 * @author Szysz
 */
public class UkladyRownanLiniowych {

    private static double A[][];
    private static double b[];

    public static void main(String[] args) {
        int num;
        int i, j, k;

        Scanner dane = new Scanner(System.in);
        System.out.println("Metoda Jacobiego");
        System.out.println("Rozwiazywanie ukladu n-rownan z n-niewiadomymi Ax=b");
        System.out.println("Podaj n");
        num = dane.nextInt();
        if (num < 1) {
            System.out.println("Nieprawidlowa warosc parametru n");
            return;
        }

        A = new double[num][num];
        b = new double[num];

        for (i = 0; i < num; i++) {
            for (j = 0; j < num; j++) {
                System.out.println("A[" + (i + 1) + "][" + (j + 1) + "] = ");
                A[i][j] = dane.nextDouble();
                if ((i == j) && (A[i][j] == 0)) {
                    System.out.println("Wartosci na przekatnej musza byc rozne od 0");
                    return;
                }
            }
        }

        for (i = 0; i < num; i++) {
            System.out.println("b[" + (i + 1) + "] = ");
            b[i] = dane.nextDouble();
        }

        double x[] = gaussElimination(num, A, b);
        System.out.println("Wyniki metoda gaussa");
        for (i = 0; i < num; i++) {
            System.out.println("x[" + i + "] = " + x[i]);
        }
        x = jacobiMethod(num, A, b);
        System.out.println("Wyniki metoda jacobiego");
        for (i = 0; i < num; i++) {
            System.out.println("x[" + i + "] = " + x[i]);
        }

    }

    public static double[] jacobiMethod(int n, double[][] A, double[] b) {

        int iter = 50;
        int i, j, k;
        double[][] M = new double[n][n];
        double[] N = new double[n];
        double[] x1 = new double[n];
        double[] x2 = new double[n];

        for (i = 0; i < n; i++) {
            N[i] = 1 / A[i][i];
        }

        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (i == j) {
                    M[i][j] = 0;
                } else {
                    M[i][j] = -(A[i][j] * N[i]);
                }
            }
        }

        for (i = 0; i < n; i++) {
            x1[i] = 0;
        }

        for (k = 0; k < iter; k++) {
            for (i = 0; i < n; i++) {
                x2[i] = N[i] * b[i];
                for (j = 0; j < n; j++) {
                    x2[i] += M[i][j] * x1[j];
                }
            }
            for (i = 0; i < n; i++) {
                x1[i] = x2[i];
            }
        }

        return x1;
    }

    public static double[] gaussElimination(int n, double[][] gik, double[] rok) {

        boolean r = false;
        double m, s, e;
        e = Math.pow(10, -12);
        double[] tabResult = new double[n];

        double[][] tabAB = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tabAB[j][i] = gik[j][i];
            }
        }

        for (int i = 0; i < n; i++) {
            tabAB[i][n] = rok[i];
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(tabAB[i][i]) < e) {
                    System.err.println("dzielnik rowny 0");
                    break;
                }

                m = -tabAB[j][i] / tabAB[i][i];
                for (int k = 0; k < n + 1; k++) {
                    tabAB[j][k] += m * tabAB[i][k];
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            s = tabAB[i][n];
            for (int j = n - 1; j >= 0; j--) {
                s -= tabAB[i][j] * tabResult[j];
            }
            if (Math.abs(tabAB[i][i]) < e) {
                System.err.println("dzielnik rowny 0");
                break;
            }
            tabResult[i] = s / tabAB[i][i];
            r = true;
        }
        return tabResult;
    }
}
