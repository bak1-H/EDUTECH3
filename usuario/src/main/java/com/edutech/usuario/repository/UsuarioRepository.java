package com.edutech.usuario.repository;

import com.edutech.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por email
    Optional<Usuario> findByEmail(String email);
    
    // Verificar si existe por email
    boolean existsByEmail(String email);
    
    // Buscar por tipo de usuario
    java.util.List<Usuario> findByTipoUsuarioId(Long tipoUsuarioId);
}