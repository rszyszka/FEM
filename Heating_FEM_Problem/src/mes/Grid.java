/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Szysz
 */
public class Grid {

    Node ND[];
    Element EL[];
    GlobalData GD;

    private static Grid grid = null;

    private Grid() throws FileNotFoundException {
        GD = GlobalData.getInstance();
        ND = new Node[GD.getNh()];
        EL = new Element[GD.getNe()];

        double dbScianki = GD.getB_scianki_stali() / (GD.getnB_scianki_stali() - 1);
        double dhWody = GD.getH_wody() / (GD.getnH_wody() - 1);
        double dbWody = GD.getB_wody() / (GD.getnB_wody() - 1);
        double dhZeliwa = GD.getH_zeliwa() / (GD.getnH_zeliwa() - 1);

        int k = 0;
        int i1 = 1, i2 = 1, j1 = 1, j2 = 1, j3 = 1;

        for (int i = 0; i < GD.getnB(); i++) {
            j1 = 1;
            j2 = 1;
            j3 = 1;
            for (int j = 0; j < GD.getnH(); j++) {

                if (j < GD.getnH_zeliwa() && i < GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(i * dbScianki, j * dhZeliwa);
                } else if (j < GD.getnH_zeliwa() + GD.getnB_scianki_stali() - 1 && i < GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(i * dbScianki, GD.getH_zeliwa() + j1++ * dbScianki);
                } else if (j < GD.getnH_zeliwa() + GD.getnB_scianki_stali() + GD.getnH_wody() - 2 && i < GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(i * dbScianki, GD.getH_zeliwa() + GD.getB_scianki_stali() + j2++ * dhWody);
                } else if (i < GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(i * dbScianki, GD.getH_zeliwa() + GD.getB_scianki_stali() + GD.getH_wody() + j3++ * dbScianki);
                } else if (j < GD.getnH_zeliwa() && i > GD.getnB() - GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(GD.getB_scianki_stali() + GD.getB_wody() + i1 * dbScianki, j * dhZeliwa);
                } else if (j < GD.getnH_zeliwa() + GD.getnB_scianki_stali() - 1 && i > GD.getnB() - GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(GD.getB_scianki_stali() + GD.getB_wody() + i1 * dbScianki, GD.getH_zeliwa() + j1++ * dbScianki);
                } else if (j < GD.getnH_zeliwa() + GD.getnB_scianki_stali() + GD.getnH_wody() - 2 && i > GD.getnB() - GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(GD.getB_scianki_stali() + GD.getB_wody() + i1 * dbScianki, GD.getH_zeliwa() + GD.getB_scianki_stali() + j2++ * dhWody);
                } else if (i > GD.getnB() - GD.getnB_scianki_stali()) {
                    ND[k++] = new Node(GD.getB_scianki_stali() + GD.getB_wody() + i1 * dbScianki, GD.getH_zeliwa() + GD.getB_scianki_stali() + GD.getH_wody() + j3++ * dbScianki);
                } else if (j < GD.getnH_zeliwa()) {
                    ND[k++] = new Node(GD.getB_scianki_stali() + i2 * dbWody, j * dhZeliwa);
                } else if (j < GD.getnH_zeliwa() + GD.getnB_scianki_stali() - 1) {
                    ND[k++] = new Node(GD.getB_scianki_stali() + i2 * dbWody, GD.getH_zeliwa() + j1++ * dbScianki);
                } else if (j < GD.getnH_zeliwa() + GD.getnB_scianki_stali() + GD.getnH_wody() - 2) {
                    ND[k++] = new Node(GD.getB_scianki_stali() + i2 * dbWody, GD.getH_zeliwa() + GD.getB_scianki_stali() + j2++ * dhWody);
                } else {
                    ND[k++] = new Node(GD.getB_scianki_stali() + i2 * dbWody, GD.getH_zeliwa() + GD.getB_scianki_stali() + GD.getH_wody() + j3++ * dbScianki);
                }

            }
            if (i > GD.getnB() - GD.getnB_scianki_stali()) {
                i1++;
            } else if (i > GD.getnB_scianki_stali() - 1) {
                i2++;
            }

        }
        k = 0;
        for (int i = 0; i < GD.getnB() - 1; i++) {
            for (int j = 0; j < GD.getnH() - 1; j++) {
                EL[k] = new Element(i, j, new Node[]{ND[GD.getnH() * i + j], ND[GD.getnH() * (i + 1) + j], ND[GD.getnH() * (i + 1) + (j + 1)], ND[GD.getnH() * i + (j + 1)]});

                if (j < GD.getnH_zeliwa() - 1) {
                    EL[k].setAlfa(GD.alfaZeliwo);
                    EL[k].setC(GD.cZeliwo);
                    EL[k].setK(GD.kZeliwo);
                    EL[k].setRo(GD.roZeliwo);
                } else if (i < GD.getnB_scianki_stali() - 1 || i > GD.getnB() - GD.getnB_scianki_stali() - 1 || j < GD.getnH_zeliwa() + GD.getnB_scianki_stali() - 2 || j > GD.getnH() - GD.getnB_scianki_stali() - 1) {
                    EL[k].setAlfa(GD.alfaStal);
                    EL[k].setC(GD.cStal);
                    EL[k].setK(GD.kStal);
                    EL[k].setRo(GD.roStal);
                } else {
                    EL[k].setC(GD.cWoda);
                    EL[k].setK(GD.kWoda);
                    EL[k].setRo(GD.roWoda);
                }
                k++;
            }
        }

    }

    public static Grid getInstance() throws FileNotFoundException {
        if (grid == null) {
            grid = new Grid();
        }
        return grid;
    }

    public void usrednijTempWody() {
        double suma = 0;
        int count = 0;
        for (int i = 0; i < GD.getNe(); i++) {
            if (EL[i].getRo() == 998.00) {
                suma += EL[i].ND[0].getT() + EL[i].ND[1].getT() + EL[i].ND[2].getT() + EL[i].ND[3].getT();
                count += 4;
            }
        }
        for (int i = 0; i < GD.getNe(); i++) {
            if (EL[i].getRo() == 998.00) {
                EL[i].ND[0].setT(suma / count);
                EL[i].ND[1].setT(suma / count);
                EL[i].ND[2].setT(suma / count);
                EL[i].ND[3].setT(suma / count);
            }
        }
    }

    public void wypiszND() {

        int k = 0;
        for (int i = 0; i < GD.getnB(); i++) {
            for (int j = 0; j < GD.getnH(); j++) {
                System.out.print(ND[k].getX() + " ; " + ND[k].getY() + "\t");
                k++;
            }
            System.out.println("");
        }
    }

    public void printPoints() {
        for (int i = 0; i < GD.getNh(); i++) {
            System.out.println(ND[i].getX() + " " + ND[i].getY() + " " + 0.0);
        }
    }

    public void printCellMap() {
        for (int i = 0; i < GD.getNe(); i++) {
            System.out.println(4 + " " + EL[i].globalNodeID[0] + " " + EL[i].globalNodeID[1] + " " + EL[i].globalNodeID[2] + " " + EL[i].globalNodeID[3]);
        }
    }

    public void printCellTypes() {
        for (int i = 0; i < GD.getNe(); i++) {
            System.out.println(9);
        }
    }

    public void printTemp() {
        for (int i = 0; i < GD.getNh(); i++) {
            System.out.println(ND[i].getT());
        }
    }

    public void makeFileVTK(int counter) {
        File dir = new File("results2");
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        String name = "result" + counter;
        File result = new File("results2/" + name + ".vtk");
        if (!result.isFile()) {
            try {
                result.createNewFile();
            } catch (IOException ex) {
                System.err.println("Couldnt make a new file!");
            }
        }
        try {
            PrintWriter writer = new PrintWriter(result);
            writer.println("# vtk DataFile Version 2.0");
            writer.println("Niestacjonarne zagadnienie cieplne - ogrzewanie wody w garnku stalowym");
            writer.println("ASCII");
            writer.println("DATASET UNSTRUCTURED_GRID");
            writer.println("POINTS " + GD.getNh() + " float");
            for (int i = 0; i < GD.getNh(); i++) {
                writer.println(ND[i].getX() + " " + ND[i].getY() + " " + 0.0);
            }
            writer.println("CELLS " + GD.getNe() + " " + 5 * GD.getNe());
            for (int i = 0; i < GD.getNe(); i++) {
                writer.println(4 + " " + EL[i].globalNodeID[0] + " " + EL[i].globalNodeID[1] + " " + EL[i].globalNodeID[2] + " " + EL[i].globalNodeID[3]);
            }
            writer.println("CELL_TYPES " + GD.getNe());
            for (int i = 0; i < GD.getNe(); i++) {
                writer.println(9);
            }
            writer.println("POINT_DATA " + GD.getNh());
            writer.println("SCALARS Temperatura float 1");
            writer.println("LOOKUP_TABLE default");
            for (int i = 0; i < GD.getNh(); i++) {
                writer.println(ND[i].getT());
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File does not exists!");
        }

    }

    public void wypiszStatus() {
        int k = 0;
        for (int i = 0; i < GD.getnB(); i++) {
            for (int j = 0; j < GD.getnH(); j++) {
                System.out.print(ND[k++].getStatus() + "\t");
            }
            System.out.println("");
        }
    }

    public void wypiszEL(int i) {
        System.out.println("ELEMENT:" + i);
        for (int j = 0; j < 4; j++) {
            System.out.println("ID" + (j) + "\tglobal ID:" + EL[i].globalNodeID[j] + "\tStatus:" + EL[i].ND[j].getStatus() + "\t(" + EL[i].ND[j].getX() + ";" + EL[i].ND[j].getY() + ")");
        }
    }
    
    public void wypiszTemp(int i){
        System.out.println(EL[i].ND[0].getT()+"\t"+EL[i].ND[1].getT()+"\t"+EL[i].ND[2].getT()+"\t"+EL[i].ND[3].getT()+"\t"+EL[i].getRo());
    }

    public void wypiszSiatke() {

        int count = 0;
        for (int i = 0; i < GD.getnB() - 1; i++) {
            for (int j = 0; j < GD.getnH() - 1; j++) {
                System.out.printf("%.5f \t", grid.EL[count++].getK());
            }
            System.out.println("");
        }

    }

    public void wypiszGlobalID() {
        int count = 0;
        for (int i = 0; i < GD.getnB() - 1; i++) {
            for (int j = 0; j < GD.getnH() - 1; j++) {
                System.out.print(this.EL[count++].globalNodeID[0] + "\t");
            }
            System.out.println("");
        }
    }

}
