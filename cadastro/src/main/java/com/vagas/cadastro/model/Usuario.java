package com.vagas.cadastro.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String matricula;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataCriacao = LocalDate.now();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Curriculo> curriculos;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Vaga> vagas;

    @OneToOne
    @JoinColumn(name = "arquivo_id")
    private Arquivo arquivo;

    @ManyToOne
    @JoinColumn(name = "perfil_id")
    private Perfil perfis;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Perfil> perfis = new ArrayList<>();
        perfis.add(this.perfis);
        return perfis;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.matricula;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
