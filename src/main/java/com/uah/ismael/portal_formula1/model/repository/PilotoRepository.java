package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Piloto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PilotoRepository extends JpaRepository<Piloto, Long> {

    List<Piloto> findByNombre(String nombre);

    List<Piloto> findByNombreContainingIgnoreCase(String nombre);

    List<Piloto> findByApellidos(String apellidos);

    List<Piloto> findByApellidosContainingIgnoreCase(String apellidos);

    List<Piloto> findByNombreAndApellidos(String nombre, String apellidos);

    Piloto findBySiglas(String siglas);

    Piloto findByDorsal(Integer dorsal);

    List<Piloto> findByPais(String pais);
    
    Piloto findByTwitter(String twitter);
    
    List<Piloto> findByEquipo_Id(Long equipoId);

    boolean existsPilotoBySiglas(String siglas);

    boolean existsPilotoByDorsal(Integer dorsal);

    boolean existsPilotoByTwitter(String twitter);
}