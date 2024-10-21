package br.com.forum_hub.controller;

import br.com.forum_hub.domain.resposta.DadosListagemResposta;
import br.com.forum_hub.domain.resposta.DadosCadastroResposta;
import br.com.forum_hub.domain.resposta.DadosAtualizacaoResposta;
import br.com.forum_hub.domain.resposta.RespostaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("topicos/{idTopico}/respostas")
public class RespostaController {

    private final RespostaService service;

    public RespostaController(RespostaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DadosListagemResposta> cadastrar(@PathVariable Long idTopico, @RequestBody @Valid DadosCadastroResposta dados, UriComponentsBuilder uriBuilder){
        var resposta = service.cadastrar(dados, idTopico);
        var uri = uriBuilder.path("topicos/{idTopico}/respostas/{id}").buildAndExpand(resposta.getTopico().getId(), resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemResposta(resposta));
    }

    @PutMapping
    public ResponseEntity<DadosListagemResposta> atualizar(@RequestBody @Valid DadosAtualizacaoResposta dados){
        var resposta = service.atualizar(dados);
        return ResponseEntity.ok(new DadosListagemResposta(resposta));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DadosListagemResposta> marcarComoSolucao(@PathVariable Long id){
        var resposta = service.marcarComoSolucao(id);
        return ResponseEntity.ok(new DadosListagemResposta(resposta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
