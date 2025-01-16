package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.CircuitoDTO;
import com.uah.ismael.portal_formula1.model.entity.Circuito;
import com.uah.ismael.portal_formula1.model.repository.CircuitoRepository;
import com.uah.ismael.portal_formula1.service.CircuitoService;
import com.uah.ismael.portal_formula1.utils.PageUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CircuitoServiceImpl implements CircuitoService {

    Logger LOG = LoggerFactory.getLogger(CircuitoServiceImpl.class);

    private final ModelMapper modelMapper;
    private final CircuitoRepository circuitoRepository;

    @Autowired
    public CircuitoServiceImpl(ModelMapper modelMapper, CircuitoRepository circuitoRepository) {
        this.modelMapper = modelMapper;
        this.circuitoRepository = circuitoRepository;
    }

    @Override
    public void addCircuito(CircuitoDTO circuito) {
        if(circuitoRepository.existsByNombre(circuito.getNombre())) {
            throw new IllegalArgumentException("Ya existe un circuito con nombre '" + circuito.getNombre() + "'");
        }
        circuitoRepository.save(modelMapper.map(circuito, Circuito.class));
    }

    @Override
    public boolean updateCircuito(CircuitoDTO circuito) {
        Circuito circuitoToUpdate = circuitoRepository.findById(circuito.getId()).orElse(null);
        if(circuitoToUpdate != null){
            if(circuitoRepository.existsByNombre(circuito.getNombre()) && !circuitoToUpdate.getNombre().equals(circuito.getNombre())) {
                throw new IllegalArgumentException("Ya existe un circuito con nombre '" + circuito.getNombre() + "'");
            }
            if(circuito.getNombre() != null){
                circuitoToUpdate.setNombre(circuito.getNombre());
            }
            if(circuito.getCiudad() != null){
                circuitoToUpdate.setCiudad(circuito.getCiudad());
            }
            if(circuito.getPais() != null){
                circuitoToUpdate.setPais(circuito.getPais());
            }
            if(circuito.getTrazado() != null){
                circuitoToUpdate.setTrazado(circuito.getTrazado());
            }
            if(circuito.getNumeroVueltas() != null){
                circuitoToUpdate.setNumeroVueltas(circuito.getNumeroVueltas());
            }
            if(circuito.getLongitud() != null){
                circuitoToUpdate.setLongitud(circuito.getLongitud());
            }
            if(circuito.getCurvasLentas() != null){
                circuitoToUpdate.setCurvasLentas(circuito.getCurvasLentas());
            }
            if(circuito.getCurvasMedias() != null){
                circuitoToUpdate.setCurvasMedias(circuito.getCurvasMedias());
            }
            if(circuito.getCurvasRapidas() != null){
                circuitoToUpdate.setCurvasRapidas(circuito.getCurvasRapidas());
            }
            if(circuito.getFechaCalendario() == null && circuitoToUpdate.getFechaCalendario() != null){
                circuitoToUpdate.setFechaCalendario(null);
            }
            if(circuito.getFechaCalendario() != null
                    && !circuito.getFechaCalendario().equals(circuitoToUpdate.getFechaCalendario())
            ){
                if(circuitoRepository.existsByFechaCalendario(circuito.getFechaCalendario())){
                    throw new IllegalArgumentException("Ya existe un circuito con fecha de calendario '" + circuito.getFechaCalendario() + "'");
                }
                if(circuito.getFechaCalendario().before(new Date())){
                    throw new IllegalArgumentException("La fecha de calendario no puede ser anterior a la fecha actual");
                }
                circuitoToUpdate.setFechaCalendario(circuito.getFechaCalendario());
            }
            circuitoRepository.save(circuitoToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public void deleteCircuito(Long id) {
        if(circuitoRepository.existsById(id)){
            circuitoRepository.deleteById(id);
        }
    }

    @Override
    public CircuitoDTO getCircuitoById(Long id) {
        Circuito circuito = circuitoRepository.findById(id).orElse(null);
        if(circuito != null){
            return modelMapper.map(circuito, CircuitoDTO.class);
        }
        return null;
    }

    @Override
    public Page<CircuitoDTO> getAllCircuitos(Pageable pageable) {
        List<CircuitoDTO> circuitos = circuitoRepository.findAll().stream()
                .map(circuito -> modelMapper.map(circuito, CircuitoDTO.class))
                .sorted(CircuitoDTO.getCircuitoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, circuitos);
    }

    @Override
    public List<CircuitoDTO> getCircuitosFechaNotNull() {
        return circuitoRepository.findByFechaCalendarioIsNotNull().stream()
                .map(circuito -> modelMapper.map(circuito, CircuitoDTO.class))
                .toList();
    }

    @Override
    public List<CircuitoDTO> getCircuitosFechaNull() {
        return circuitoRepository.findByFechaCalendarioIsNull().stream()
                .map(circuito -> modelMapper.map(circuito, CircuitoDTO.class))
                .toList();
    }

}
