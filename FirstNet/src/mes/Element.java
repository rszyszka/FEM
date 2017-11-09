/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.FileNotFoundException;

/**
 *
 * @author Szysz
 */
public class Element {
    
    public Node ID[];
    
//    public Element(double x, double y, double db, double dh) throws FileNotFoundException {
//        
//        ID = new Node[4];
//        
//        ID[0] = new Node(x, y);       
//        ID[1] = new Node(x+db, y);
//        ID[2] = new Node(x+db, y+dh);
//        ID[3] = new Node(x, y+dh);
//        
//    }
    
    public Element(int i, int j, double db, double dh) throws FileNotFoundException {
        
        ID = new Node[4];
        
        ID[0] = new Node(i*db, j*dh);       
        ID[1] = new Node((i+1)*db, j*dh); 
        ID[2] = new Node((i+1)*db, (j+1)*dh);
        ID[3] = new Node(i*db, (j+1)*dh);
        
    }
    
}
