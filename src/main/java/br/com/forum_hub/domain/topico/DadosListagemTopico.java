package br.com.forum_hub.domain.topico;

import br.com.forum_hub.domain.resposta.DadosListagemResposta;

import java.time.LocalDateTime;
import java.util.List;

public record DadosListagemTopico(
        Long id,
        String titulo,
        String mensagem,
        String autor,
        Status status,
        LocalDateTime dataCriacao,
        Integer quantidadeRespostas,
        String curso
) {
    public DadosListagemTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.getAutor().getNomeUsuario(), topico.getStatus(), topico.getDataCriacao(), topico.getQuantidadeRespostas(), topico.getCurso().getNome());
    }
}
