package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.CocheDTO;
import com.uah.ismael.portal_formula1.model.entity.Coche;
import com.uah.ismael.portal_formula1.model.repository.CocheRepository;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
import com.uah.ismael.portal_formula1.service.CocheService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CocheServiceImpl implements CocheService {

    Logger LOG = LoggerFactory.getLogger(CocheServiceImpl.class);

    private final ModelMapper modelMapper;
    private final CocheRepository cocheRepository;

    public CocheServiceImpl(ModelMapper modelMapper, CocheRepository cocheRepository) {
        this.modelMapper = modelMapper;
        this.cocheRepository = cocheRepository;
    }

    @Override
    public void addCoche(CocheDTO coche) {
        if(cocheRepository.existsCocheByNombre(coche.getNombre())) {
            throw new IllegalArgumentException("Ya existe un coche con el nombre '" + coche.getNombre() + "'");
        }
        if(cocheRepository.existsCocheByCodigo(coche.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un coche con el código '" + coche.getCodigo() + "'");
        }
        cocheRepository.save(modelMapper.map(coche, Coche.class));
    }

    @Override
    public boolean updateCoche(CocheDTO coche) {
        Coche cocheToUpdate = cocheRepository.findById(coche.getId()).orElse(null);
        if (cocheToUpdate != null){
            if (cocheRepository.existsCocheByNombre(coche.getNombre()) && !cocheToUpdate.getNombre().equals(coche.getNombre())) {
                throw new IllegalArgumentException("Ya existe un coche con el nombre '" + coche.getNombre() + "'");
            }
            if (cocheRepository.existsCocheByCodigo(coche.getCodigo()) && !cocheToUpdate.getCodigo().equals(coche.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un coche con el código '" + coche.getCodigo() + "'");
            }
            if(coche.getNombre() != null) {
                cocheToUpdate.setNombre(coche.getNombre());
            }
            if(coche.getCodigo() != null) {
                cocheToUpdate.setCodigo(coche.getCodigo());
            }
            if (coche.getErsCurvalenta() != null) {
                cocheToUpdate.setErsCurvalenta(coche.getErsCurvalenta());
            }
            if (coche.getErsCurvamedia() != null) {
                cocheToUpdate.setErsCurvamedia(coche.getErsCurvamedia());
            }
            if (coche.getErsCurvarapida() != null) {
                cocheToUpdate.setErsCurvarapida(coche.getErsCurvarapida());
            }
            if (coche.getConsumo() != null) {
                cocheToUpdate.setConsumo(coche.getConsumo());
            }
            cocheRepository.save(cocheToUpdate);
        }
        return false;
    }

    @Override
    public void deleteCoche(Long id) {
        if(cocheRepository.existsById(id)) {
            cocheRepository.deleteById(id);
        }
    }

    @Override
    public Page<CocheDTO> getAllCoches(Pageable pageable) {
        List<CocheDTO> coches = cocheRepository.findAll().stream()
                .map(coche -> modelMapper.map(coche, CocheDTO.class))
                .sorted(CocheDTO.getCochePageableComparator(pageable))
                .toList();
        return PageUtil.sortedPageImpl(pageable, coches);
    }

    @Override
    public CocheDTO getCocheById(Long id) {
        Coche coche = cocheRepository.findById(id).orElse(null);
        if(coche != null) {
            return modelMapper.map(coche, CocheDTO.class);
        }
        return null;
    }

    @Override
    public CocheDTO getCocheByNombre(String nombre) {
        Coche coche = cocheRepository.findByNombre(nombre);
        if(coche != null) {
            return modelMapper.map(coche, CocheDTO.class);
        }
        return null;
    }

    @Override
    public CocheDTO getCocheByCodigo(String codigo) {
        Coche coche = cocheRepository.findByCodigo(codigo);
        if(coche != null) {
            return modelMapper.map(coche, CocheDTO.class);
        }
        return null;
    }

    @Override
    public Page<CocheDTO> getCochesByEquipo(Long idEquipo, Pageable pageable) {
        List<CocheDTO> coches = cocheRepository.findByEquipo_Id(idEquipo).stream()
                .map(coche -> modelMapper.map(coche, CocheDTO.class))
                .sorted(CocheDTO.getCochePageableComparator(pageable))
                .toList();
        return PageUtil.sortedPageImpl(pageable, coches);
    }
}
