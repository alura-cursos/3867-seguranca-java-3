package br.com.forum_hub.domain.topico;

import br.com.forum_hub.domain.resposta.DadosListagemResposta;
import br.com.forum_hub.domain.resposta.Resposta;

import java.util.List;

public record DadosDetalhesTopico(DadosListagemTopico dadosListagem, List<DadosListagemResposta> respostas) {
    public DadosDetalhesTopico(Topico topico, List<Resposta> respostas) {
        this(new DadosListagemTopico(topico), respostas.stream().map(DadosListagemResposta::new).toList());
    }
}