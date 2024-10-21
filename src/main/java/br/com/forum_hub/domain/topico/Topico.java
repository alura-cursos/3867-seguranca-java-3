package br.com.forum_hub.domain.topico;

import br.com.forum_hub.domain.curso.Categoria;
import br.com.forum_hub.domain.curso.Curso;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "topicos")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensagem;
    private String autor;
    private LocalDateTime dataCriacao;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Boolean aberto;
    private Integer quantidadeRespostas;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Deprecated
    public Topico(){}

    public Topico(DadosCadastroTopico dados, Curso curso) {
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.autor = dados.autor();
        this.dataCriacao = LocalDateTime.now();
        this.status = Status.NAO_RESPONDIDO;
        this.aberto = true;
        this.quantidadeRespostas = 0;
        this.categoria = curso.getCategoria();
        this.curso = curso;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
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

    public Status getStatus() {
        return status;
    }

    public Curso getCurso() {
        return curso;
    }

    public Integer getQuantidadeRespostas() {
        return quantidadeRespostas;
    }

    public Topico atualizarInformacoes(DadosAtualizacaoTopico dados, Curso curso) {
        if(dados.titulo() != null){
            this.titulo = dados.titulo();
        }
        if(dados.mensagem() != null){
            this.mensagem = dados.mensagem();
        }
        if(curso != null){
            this.curso = curso;
        }
        return this;
    }

    public void alterarStatus(Status status) {
        this.status = status;
    }

    public void incrementarRespostas() {
        this.quantidadeRespostas++;
    }

    public void decrementarRespostas() {
        this.quantidadeRespostas--;
    }

    public void fechar() {
        this.aberto = false;
    }

    public boolean estaAberto() {
        return this.aberto;
    }
}