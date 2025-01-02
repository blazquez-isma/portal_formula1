package com.uah.ismael.portal_formula1.model.repository;

import com.uah.ismael.portal_formula1.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    boolean existsByNombreUsuario(@Param("nombreUsuario") String nombreUsuario);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Usuario u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    Usuario findByNombre(String nombre);

    Usuario findByNombreUsuario(String nombreUsuario);

    Usuario findByEmail(String email);

    List<Usuario> findByActivo(boolean activo);

    List<Usuario> findByEquipo_Id(Long equipoId);

}