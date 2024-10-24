package br.com.forum_hub.domain.topico;

import br.com.forum_hub.domain.curso.CursoService;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    private final TopicoRepository repository;
    private final CursoService cursoService;

    public TopicoService(TopicoRepository repository, CursoService cursoService) {
        this.repository = repository;
        this.cursoService = cursoService;
    }

    @Transactional
    public Topico cadastrar(DadosCadastroTopico dados, Usuario autor) {
        var curso = cursoService.buscarPeloId(dados.cursoId());
        var topico = new Topico(dados, curso, autor);
        return repository.save(topico);
    }
    public Page<DadosListagemTopico> listar(String categoria, Long idCurso, Boolean semResposta, Boolean solucionados, Pageable paginacao) {
        Specification<Topico> spec = Specification.where(TopicoSpecification.estaAberto())
                .and(TopicoSpecification.temCategoria(categoria))
                .and(TopicoSpecification.temCursoId(idCurso))
                .and(TopicoSpecification.estaSemResposta(semResposta))
                .and(TopicoSpecification.estaSolucionado(solucionados));

        Page<Topico> topicos = repository.findAll(spec, paginacao);
        return topicos.map(DadosListagemTopico::new);
    }

    @Transactional
    public Topico atualizar(DadosAtualizacaoTopico dados) {
        var topico = buscarPeloId(dados.id());
        var curso = cursoService.buscarPeloId(dados.cursoId());
        return topico.atualizarInformacoes(dados, curso);
    }

    @Transactional
    public void excluir(Long id) {
        var topico = buscarPeloId(id);
        if (topico.getStatus() == Status.NAO_RESPONDIDO)
            repository.deleteById(id);
        else
            throw new RegraDeNegocioException("Você não pode apagar um tópico que já foi respondido.");
    }

    public Topico buscarPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Tópico não encontrado!"));

    }

    @Transactional
    public void fechar(Long id) {
        var topico = buscarPeloId(id);
        topico.fechar();
    }
}
