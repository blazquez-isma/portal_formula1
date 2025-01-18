package com.uah.ismael.portal_formula1.service;

import com.uah.ismael.portal_formula1.dto.CircuitoDTO;
import com.uah.ismael.portal_formula1.dto.CocheDTO;

public interface SimulacionService {
    double calcularConsumoPorVuelta(CocheDTO coche, CircuitoDTO circuito);
    double calcularConsumoTotal(CocheDTO coche, CircuitoDTO circuito, int numeroVueltas);
    double calcularERSPorVuelta(CocheDTO coche, CircuitoDTO circuito, String estiloConduccionStr);
    int calcularVueltasParaCargarBateria(CocheDTO coche, CircuitoDTO circuito, String estiloConduccionStr);
}
