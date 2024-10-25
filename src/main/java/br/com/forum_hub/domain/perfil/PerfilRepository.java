package br.com.forum_hub.domain.perfil;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Perfil findByNome(PerfilNome perfilNome);
}
