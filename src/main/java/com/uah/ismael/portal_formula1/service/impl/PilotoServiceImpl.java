package com.uah.ismael.portal_formula1.service.impl;

import com.uah.ismael.portal_formula1.dto.PilotoDTO;
import com.uah.ismael.portal_formula1.model.entity.Piloto;
import com.uah.ismael.portal_formula1.model.repository.PilotoRepository;
import com.uah.ismael.portal_formula1.paginator.PageUtil;
import com.uah.ismael.portal_formula1.service.PilotoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PilotoServiceImpl implements PilotoService {

    Logger LOG = LoggerFactory.getLogger(PilotoServiceImpl.class);

    private final ModelMapper modelMapper;
    private final PilotoRepository pilotoRepository;

    @Autowired
    public PilotoServiceImpl(ModelMapper modelMapper, PilotoRepository pilotoRepository) {
        this.modelMapper = modelMapper;
        this.pilotoRepository = pilotoRepository;
    }

    @Override
    public void addPiloto(PilotoDTO piloto) {
        if(pilotoRepository.existsPilotoBySiglas(piloto.getSiglas())) {
            throw new IllegalArgumentException("Ya existe un piloto con las siglas '" + piloto.getSiglas() + "'");
        }
        if(pilotoRepository.existsPilotoByDorsal(piloto.getDorsal())) {
            throw new IllegalArgumentException("Ya existe un piloto con el dorsal '" + piloto.getDorsal() + "'");
        }
        if(pilotoRepository.existsPilotoByTwitter(piloto.getTwitter())) {
            throw new IllegalArgumentException("Ya existe un piloto con el twitter '" + piloto.getTwitter() + "'");
        }
        pilotoRepository.save(modelMapper.map(piloto, Piloto.class));
    }

    @Override
    public boolean updatePiloto(PilotoDTO piloto) {
        Piloto pilotoToUpdate = pilotoRepository.findById(piloto.getId()).orElse(null);
        if(pilotoToUpdate != null){
            if (pilotoRepository.existsPilotoBySiglas(piloto.getSiglas()) && !pilotoToUpdate.getSiglas().equals(piloto.getSiglas())) {
                throw new IllegalArgumentException("Ya existe un piloto con las siglas '" + piloto.getSiglas() + "'");
            }
            if (pilotoRepository.existsPilotoByDorsal(piloto.getDorsal()) && !pilotoToUpdate.getDorsal().equals(piloto.getDorsal())) {
                throw new IllegalArgumentException("Ya existe un piloto con el dorsal '" + piloto.getDorsal() + "'");
            }
            if (pilotoRepository.existsPilotoByTwitter(piloto.getTwitter()) && !pilotoToUpdate.getTwitter().equals(piloto.getTwitter())) {
                throw new IllegalArgumentException("Ya existe un piloto con el twitter '" + piloto.getTwitter() + "'");
            }
            if(piloto.getNombre() != null) {
                pilotoToUpdate.setNombre(piloto.getNombre());
            }
            if(piloto.getApellidos() != null) {
                pilotoToUpdate.setApellidos(piloto.getApellidos());
            }
            if(piloto.getSiglas() != null) {
                pilotoToUpdate.setSiglas(piloto.getSiglas());
            }
            if(piloto.getDorsal() != null) {
                pilotoToUpdate.setDorsal(piloto.getDorsal());
            }
            if(piloto.getPais() != null) {
                pilotoToUpdate.setPais(piloto.getPais());
            }
            if(piloto.getTwitter() != null) {
                pilotoToUpdate.setTwitter(piloto.getTwitter());
            }
            if (piloto.getFoto() != null) {
                pilotoToUpdate.setFoto(piloto.getFoto());
            }
            pilotoRepository.save(pilotoToUpdate);
        }
        return false;
    }

    @Override
    public void deletePiloto(Long id) {
        if(pilotoRepository.existsById(id)) {
            pilotoRepository.deleteById(id);
        }
    }

    @Override
    public Page<PilotoDTO> getAllPilotos(Pageable pageable) {
        List<PilotoDTO> pilotos = pilotoRepository.findAll().stream()
                .map(piloto -> modelMapper.map(piloto, PilotoDTO.class))
                .sorted(PilotoDTO.getPilotoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, pilotos);
    }

    @Override
    public PilotoDTO getPilotoById(Long id) {
        Piloto piloto = pilotoRepository.findById(id).orElse(null);
        if(piloto != null) {
            return modelMapper.map(piloto, PilotoDTO.class);
        }
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotosByNombre(Pageable pageable, String nombre) {
        List<PilotoDTO> pilotos = pilotoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(piloto -> modelMapper.map(piloto, PilotoDTO.class))
                .sorted(PilotoDTO.getPilotoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, pilotos);
    }

    @Override
    public Page<PilotoDTO> getPilotosByApellido(Pageable pageable, String apellidos) {
        List<PilotoDTO> pilotos = pilotoRepository.findByApellidosContainingIgnoreCase(apellidos).stream()
                .map(piloto -> modelMapper.map(piloto, PilotoDTO.class))
                .sorted(PilotoDTO.getPilotoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, pilotos);
    }

    @Override
    public Page<PilotoDTO> getPilotosByNombreAndApellidos(Pageable pageable, String nombre, String apellidos) {
        List<PilotoDTO> pilotos = pilotoRepository.findByNombreAndApellidos(nombre, apellidos).stream()
                .map(piloto -> modelMapper.map(piloto, PilotoDTO.class))
                .sorted(PilotoDTO.getPilotoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, pilotos);
    }

    @Override
    public PilotoDTO getPilotoBySiglas(String siglas) {
        Piloto piloto = pilotoRepository.findBySiglas(siglas);
        if (piloto != null) {
            return modelMapper.map(piloto, PilotoDTO.class);
        }
        return null;
    }

    @Override
    public PilotoDTO getPilotoByDorsal(Integer dorsal) {
        Piloto piloto = pilotoRepository.findByDorsal(dorsal);
        if (piloto != null) {
            return modelMapper.map(piloto, PilotoDTO.class);
        }
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotosByPais(Pageable pageable, String pais) {
        List<PilotoDTO> pilotos = pilotoRepository.findByPais(pais).stream()
                .map(piloto -> modelMapper.map(piloto, PilotoDTO.class))
                .sorted(PilotoDTO.getPilotoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, pilotos);
    }

    @Override
    public PilotoDTO getPilotoByTwitter(String twitter) {
        Piloto piloto = pilotoRepository.findByTwitter(twitter);
        if (piloto != null) {
            return modelMapper.map(piloto, PilotoDTO.class);
        }
        return null;
    }

    @Override
    public Page<PilotoDTO> getPilotosByEquipoId(Pageable pageable, Long equipoId) {
        List<PilotoDTO> pilotos = pilotoRepository.findByEquipo_Id(equipoId).stream()
                .map(piloto -> modelMapper.map(piloto, PilotoDTO.class))
                .sorted(PilotoDTO.getPilotoPageableComparator(pageable))
                .toList();

        return PageUtil.sortedPageImpl(pageable, pilotos);
    }
}
