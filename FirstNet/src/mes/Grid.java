/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.FileNotFoundException;

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

        double db = GD.getB() / (GD.getnB() - 1);
        double dh = GD.getH() / (GD.getnH() - 1);

        int k = 0;
        for (int i = 0; i < GD.getnB(); i++) {
            for (int j = 0; j < GD.getnH(); j++) {
                ND[k++] = new Node(i * db, j * dh);
            }
        }
        k = 0;
        for (int i = 0; i < GD.getnB() - 1; i++) {
            for (int j = 0; j < GD.getnH() - 1; j++) {
                EL[k++] = new Element(i, j, new Node[]{ND[GD.getnH() * i + j], ND[GD.getnH() * (i + 1) + j], ND[GD.getnH() * (i + 1) + (j + 1)], ND[GD.getnH() * i + (j + 1)]});
            }
        }

//        for (int i = 0; i < GD.getNe(); i++) {
//            for (int j = 0; j < 4; j++) {
//                ND[EL[i].globalNodeID[j]] = EL[i].ND[j];
//            }
//        }
    }

    public static Grid getInstance() throws FileNotFoundException {
        if (grid == null) {
            grid = new Grid();
        }
        return grid;
    }

    public void wypiszND() {
        for (int i = 0; i < GD.getNh(); i++) {
            System.out.println("i:" + i + "\tStatus:" + ND[i].getStatus() + "\t(" + ND[i].getX() + ";" + ND[i].getY() + ")");
        }
    }

    public void wypiszEL(int i) {
        System.out.println("ELEMENT:" + i);
        for (int j = 0; j < 4; j++) {
            System.out.println("ID" + (j) + "\tglobal ID:" + EL[i].globalNodeID[j] + "\tStatus:" + EL[i].ND[j].getStatus() + "\t(" + EL[i].ND[j].getX() + ";" + EL[i].ND[j].getY() + ")");
        }
    }
}
