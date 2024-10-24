package br.com.forum_hub.controller;

import br.com.forum_hub.domain.resposta.RespostaService;
import br.com.forum_hub.domain.topico.DadosAtualizacaoTopico;
import br.com.forum_hub.domain.topico.DadosCadastroTopico;
import br.com.forum_hub.domain.topico.DadosDetalhesTopico;
import br.com.forum_hub.domain.topico.DadosListagemTopico;
import br.com.forum_hub.domain.topico.TopicoService;
import br.com.forum_hub.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("topicos")
public class TopicoController {
    private final TopicoService service;
    private final RespostaService respostaService;

    public TopicoController(TopicoService service, RespostaService respostaService) {
        this.service = service;
        this.respostaService = respostaService;
    }

    @PostMapping
    public ResponseEntity<DadosListagemTopico> cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal Usuario autor){
        var topico = service.cadastrar(dados, autor);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @RequestParam(required = false) Long curso,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false, name = "sem-resposta") Boolean semResposta,
            @RequestParam(required = false) Boolean solucionados,
            @PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao){

        var pagina = service.listar(categoria, curso, semResposta,
                solucionados, paginacao);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhesTopico> detalhar(@PathVariable Long id){
        var topico = service.buscarPeloId(id);
        var respostas = respostaService.buscarRespostasTopico(id);
        return ResponseEntity.ok(new DadosDetalhesTopico(topico, respostas));
    }

    @PutMapping
    public ResponseEntity<DadosListagemTopico> atualizar(@RequestBody @Valid DadosAtualizacaoTopico dados){
        var topico = service.atualizar(dados);
        return ResponseEntity.ok(new DadosListagemTopico(topico));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> fechar(@PathVariable Long id){
        service.fechar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
