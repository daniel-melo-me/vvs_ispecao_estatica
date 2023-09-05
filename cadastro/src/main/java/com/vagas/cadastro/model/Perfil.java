package com.vagas.cadastro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "perfil")
public class Perfil implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private PerfilEnum nome;

    @JsonIgnore
    @OneToMany(mappedBy = "perfis")
    private List<Usuario> usuarios;

    @Override
    public String getAuthority() {
        return nome.toString();
    }

}
