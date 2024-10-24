package br.com.forum_hub.domain.usuario;

import java.time.LocalDate;

public record DadosListagemUsuario(Long id,
                                   String email,
                                   String nomeCompleto,
                                   String nomeUsuario,
                                   String miniBiografia,
                                   String biografia
) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getUsername(),
                usuario.getNomeCompleto(), usuario.getNomeUsuario(),
                usuario.getMiniBiografia(), usuario.getBiografia());
    }
}