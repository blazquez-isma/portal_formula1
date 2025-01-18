package com.uah.ismael.portal_formula1.utils;

public enum EstiloConduccion {
    AHORRADOR(1.05),
    NORMAL(0.75),
    DEPORTIVO(0.40);

    private final double modificador;

    EstiloConduccion(double modificador) {
        this.modificador = modificador;
    }

    public double getModificador() {
        return modificador;
    }
}
