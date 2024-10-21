package br.com.forum_hub.domain.resposta;

import br.com.forum_hub.domain.topico.Status;
import br.com.forum_hub.domain.topico.Topico;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "respostas")
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;
    private String autor;
    private LocalDateTime dataCriacao;
    private Boolean solucao;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @Deprecated
    public Resposta(){}

    public Resposta(DadosCadastroResposta dados, Topico topico) {
        this.mensagem = dados.mensagem();
        this.autor = dados.autor();
        this.dataCriacao = LocalDateTime.now();
        this.solucao = false;
        this.topico = topico;
    }

    public Long getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getAutor() {
        return autor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Boolean ehSolucao() {
        return solucao;
    }

    public Topico getTopico() {
        return topico;
    }

    public Resposta atualizarInformacoes(DadosAtualizacaoResposta dados) {
        this.mensagem = dados.mensagem();
        return this;
    }

    public Resposta marcarComoSolucao() {
        this.solucao = true;
        return this;
    }
}
