package br.com.forum_hub.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCaseAndVerificadoTrue(String email);

    Optional<Usuario> findByToken(String codigo);

    Optional<Usuario> findByNomeUsuarioIgnoreCaseAndVerificadoTrueAndAtivoTrue(String nomeUsuario);
}
