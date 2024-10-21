package br.com.forum_hub.domain.resposta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoResposta(
        @NotNull Long id,
        @NotBlank String mensagem
) {
}
