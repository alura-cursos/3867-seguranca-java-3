package br.com.forum_hub.domain.perfil;

import jakarta.validation.constraints.NotNull;

public record DadosPerfil(@NotNull PerfilNome perfilNome) {
}
