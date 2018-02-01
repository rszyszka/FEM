/*
 * Copyright (C) 2017 Szysz
 */
package mes;

/**
 *
 * @author Szysz
 */
public class Surface {

    private final Node[] ND;

    public Surface(Node node1, Node node2) {

        ND = new Node[2];
        this.ND[0] = node1;
        this.ND[1] = node2;
    }

    public Node[] getNodes() {
        return ND;
    }
}
