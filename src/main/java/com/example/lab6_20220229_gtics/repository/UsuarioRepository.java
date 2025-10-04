package com.example.lab6_20220229_gtics.repository;
import com.example.lab6_20220229_gtics.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);
}
