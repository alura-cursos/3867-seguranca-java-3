package br.com.forum_hub.domain.resposta;

import br.com.forum_hub.domain.topico.Status;
import br.com.forum_hub.domain.topico.TopicoService;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespostaService {
    private final RespostaRepository repository;
    private final TopicoService topicoService;

    public RespostaService(RespostaRepository repository, TopicoService topicoService) {
        this.repository = repository;
        this.topicoService = topicoService;
    }

    @Transactional
    public Resposta cadastrar(DadosCadastroResposta dados, Long idTopico) {
        var topico = topicoService.buscarPeloId(idTopico);

        if(!topico.estaAberto()) {
            throw new RegraDeNegocioException("O tópico está fechado! Você não pode adicionar mais respostas.");
        }

        if(topico.getQuantidadeRespostas() == 0) {
            topico.alterarStatus(Status.RESPONDIDO);
        }

        topico.incrementarRespostas();

        var resposta = new Resposta(dados, topico);
        return repository.save(resposta);
    }

    @Transactional
    public Resposta atualizar(DadosAtualizacaoResposta dados) {
        var resposta = buscarPeloId(dados.id());
        return resposta.atualizarInformacoes(dados);
    }

    public List<Resposta> buscarRespostasTopico(Long id){
        return repository.findByTopicoId(id);
    }

    @Transactional
    public Resposta marcarComoSolucao(Long id) {
        var resposta = buscarPeloId(id);

        var topico = resposta.getTopico();
        if(topico.getStatus() == Status.RESOLVIDO)
            throw new RegraDeNegocioException("O tópico já foi solucionado! Você não pode marcar mais de uma resposta como solução.");

        topico.alterarStatus(Status.RESOLVIDO);
        return resposta.marcarComoSolucao();
    }

    @Transactional
    public void excluir(Long id) {
        var resposta = buscarPeloId(id);
        var topico = resposta.getTopico();

        repository.deleteById(id);

        topico.decrementarRespostas();
        if (topico.getQuantidadeRespostas() == 0)
            topico.alterarStatus(Status.NAO_RESPONDIDO);
        else if(resposta.ehSolucao())
            topico.alterarStatus(Status.RESPONDIDO);
    }

    public Resposta buscarPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Resposta não encontrada!"));
    }
}
