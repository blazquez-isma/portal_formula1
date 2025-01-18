package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.CircuitoDTO;
import com.uah.ismael.portal_formula1.dto.CocheDTO;
import com.uah.ismael.portal_formula1.service.SimulacionService;
import com.uah.ismael.portal_formula1.utils.Constants;
import com.uah.ismael.portal_formula1.utils.EstiloConduccion;
import org.springframework.stereotype.Service;

@Service
public class SimulacionServiceImpl implements SimulacionService {
    @Override
    public double calcularConsumoPorVuelta(CocheDTO coche, CircuitoDTO circuito) {
        return coche.getConsumo() * (circuito.getLongitud() / 1000.0);
    }

    @Override
    public double calcularConsumoTotal(CocheDTO coche, CircuitoDTO circuito, int numeroVueltas) {
        double consumoPorVuelta = this.calcularConsumoPorVuelta(coche, circuito);
        return consumoPorVuelta * numeroVueltas;
    }

    @Override
    public double calcularERSPorVuelta(CocheDTO coche, CircuitoDTO circuito, String estiloConduccionStr) {
        EstiloConduccion estiloConduccion = EstiloConduccion.valueOf(estiloConduccionStr.toUpperCase());

        double energiaRecuperada =
                coche.getErsCurvalenta() * circuito.getCurvasLentas() +
                coche.getErsCurvamedia() * circuito.getCurvasMedias() +
                coche.getErsCurvarapida() * circuito.getCurvasRapidas();
        energiaRecuperada = Math.min(energiaRecuperada, Constants.LIMITE_ENERGIA_VUELTA);
        energiaRecuperada *= estiloConduccion.getModificador();
        return energiaRecuperada;
    }

    @Override
    public int calcularVueltasParaCargarBateria(CocheDTO coche, CircuitoDTO circuito, String estiloConduccionStr) {
        EstiloConduccion estiloConduccion = EstiloConduccion.valueOf(estiloConduccionStr.toUpperCase());
        double energiaPorVuelta = this.calcularERSPorVuelta(coche, circuito, estiloConduccion.name());

        if (energiaPorVuelta == 0) {
            return Integer.MAX_VALUE; // No es posible cargar la batería
        }
        return (int) Math.ceil(Constants.CAPACIDAD_BATERIA / energiaPorVuelta);
    }
}
