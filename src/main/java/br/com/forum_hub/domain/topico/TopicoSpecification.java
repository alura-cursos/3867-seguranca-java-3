package br.com.forum_hub.domain.topico;

import org.springframework.data.jpa.domain.Specification;

public class TopicoSpecification {

    public static Specification<Topico> temCategoria(String categoria) {
        return (root, query, builder) -> categoria == null ? null : builder.equal(root.get("categoria"), categoria);
    }

    public static Specification<Topico> temCursoId(Long idCurso) {
        return (root, query, builder) -> idCurso == null ? null : builder.equal(root.get("curso").get("id"), idCurso);
    }

    public static Specification<Topico> estaAberto() {
        return (root, query, builder) -> builder.isTrue(root.get("aberto"));
    }

    public static Specification<Topico> estaSemResposta(Boolean semResposta) {
        return (root, query, builder) -> (semResposta == null || !semResposta) ? null : builder.equal(root.get("status"), Status.NAO_RESPONDIDO);
    }

    public static Specification<Topico> estaSolucionado(Boolean solucionados) {
        return (root, query, builder) -> (solucionados == null || !solucionados) ? null : builder.equal(root.get("status"), Status.RESOLVIDO);
    }
}

