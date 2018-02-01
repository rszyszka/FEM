/*
 * Copyright (C) 2017 Szysz
 */
package lokal;

/**
 *
 * @author Szysz
 */
public class LokalSurface {

    public final LokalNode[] ND;

    public final double N[][];

    public LokalSurface(LokalNode node1, LokalNode node2) {
        ND = new LokalNode[2];

        ND[0] = node1;
        ND[1] = node2;

        N = new double[2][4];
    }
}
