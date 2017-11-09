/*
 * Copyright (C) 2017 Szysz
 */
package mes;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
         grid.wypiszEL(0);
         System.out.println("\n");
         grid.wypiszEL(gd.getnH()-2);
         System.out.println("\n");
         grid.wypiszEL(gd.getNe()-(gd.getnH()-1));
         System.out.println("\n");
         grid.wypiszEL(gd.getNe()-1);

        
    }
    
}
