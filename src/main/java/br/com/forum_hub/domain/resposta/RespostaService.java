package br.com.forum_hub.domain.resposta;

import br.com.forum_hub.domain.topico.Status;
import br.com.forum_hub.domain.topico.TopicoService;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespostaService {
    private final RespostaRepository repository;
    private final TopicoService topicoService;
    private final RoleHierarchy roleHierarchy;

    public RespostaService(RespostaRepository repository, TopicoService topicoService, RoleHierarchy roleHierarchy) {
        this.repository = repository;
        this.topicoService = topicoService;
        this.roleHierarchy = roleHierarchy;
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
    public Resposta marcarComoSolucao(Long id, Usuario logado) {
        var resposta = buscarPeloId(id);

        var topico = resposta.getTopico();

        if(!usuarioTemPermissoes(logado, topico.getAutor()))
            throw new RegraDeNegocioException("Você não pode marcar essa resposta como solução!");

        if(topico.getStatus() == Status.RESOLVIDO)
            throw new RegraDeNegocioException("O tópico já foi solucionado! Você não pode marcar mais de uma resposta como solução.");

        topico.alterarStatus(Status.RESOLVIDO);
        return resposta.marcarComoSolucao();
    }

    private boolean usuarioTemPermissoes(Usuario logado, Usuario autor) {
        for(GrantedAuthority autoridade: logado.getAuthorities()){
            var autoridadesAlcancaveis =  roleHierarchy.getReachableGrantedAuthorities(List.of(autoridade));

            for(GrantedAuthority perfil: autoridadesAlcancaveis){
                if(perfil.getAuthority().equals("ROLE_INSTRUTOR") || logado.getId().equals(autor.getId()))
                    return true;
            }
        }
        return false;
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
