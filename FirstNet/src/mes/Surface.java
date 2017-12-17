/*
 * Copyright (C) 2017 Szysz
 */
package mes;

/**
 *
 * @author Szysz
 */
public class Surface {

    private final Node[] ID;

    public Surface(Node node1, Node node2) {

        ID = new Node[2];
        this.ID[0] = node1;
        this.ID[1] = node2;
    }

    public Node[] getNodes() {
        return ID;
    }
}
