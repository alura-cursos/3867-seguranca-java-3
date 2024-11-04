package br.com.forum_hub.infra.seguranca;

import br.com.forum_hub.domain.usuario.Usuario;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HierarquiaService {
    private final RoleHierarchy roleHierarchy;

    public HierarquiaService(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    public boolean usuarioNaoTemPermissoes(Usuario logado, Usuario autor, String perfilDesejado) {
        for(GrantedAuthority autoridade: logado.getAuthorities()){
            var autoridadesAlcancaveis =  roleHierarchy.getReachableGrantedAuthorities(List.of(autoridade));

            for(GrantedAuthority perfil: autoridadesAlcancaveis){
                if(perfil.getAuthority().equals(perfilDesejado) || logado.getId().equals(autor.getId()))
                    return false;
            }
        }
        return true;
    }
}
