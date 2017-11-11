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

    public Grid() throws FileNotFoundException {
        GD = new GlobalData();
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
                EL[k++] = new Element(i, j, db, dh);
            }
        }
    }

    public void wypiszND() {
        for (int i = 0; i < GD.getNh(); i++) {
            System.out.println("i:" + i + "\tStatus:" + ND[i].getStatus() + "\t(" + ND[i].getX() + ";" + ND[i].getY() + ")");
        }
    }

    public void wypiszEL(int i) {
        for (int j = 0; j < 4; j++) {
            System.out.println("ID" + j + "\tStatus:" + EL[i].ID[j].getStatus() + "\t(" + EL[i].ID[j].getX() + ";" + EL[i].ID[j].getY() + ")");
        }
    }
}
