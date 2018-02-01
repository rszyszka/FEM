/*
 * Copyright (C) 2017 Szysz
 */
package lokal;

/**
 *
 * @author Szysz
 */
public class LokalNode {

    private final double ksi;
    private final double eta;

    public LokalNode(double ksi, double eta) {
        this.ksi = ksi;
        this.eta = eta;
    }

    public double getKsi() {
        return ksi;
    }

    public double getEta() {
        return eta;
    }

}
