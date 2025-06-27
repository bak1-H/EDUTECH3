package com.edutech.usuario.repository;
import com.edutech.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface repository_usuario extends JpaRepository<Usuario, Long> {
    
}