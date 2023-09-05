package com.vagas.cadastro.config.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import com.vagas.cadastro.model.*;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.vagas.cadastro.config.request.UsuarioEditRequestDTO.getUsuario;

@Data
public class UsuarioRequestDTO {

    private PasswordEncoder encoder;

    private Long id;
    private List<Curriculo> curriculos;
    private Arquivo arquivo;
    @NotNull
    private String nome;
    @NotNull
    private String email;
    @NotNull
    private String senha;
    @NotNull
    private String matricula;
    @JsonIgnore
    private List<Vaga> vagas;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.curriculos = usuario.getCurriculos();
        this.arquivo = usuario.getArquivo();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = encoder.encode(usuario.getSenha());
        this.matricula = usuario.getMatricula();
        this.vagas = usuario.getVagas();
    }

    public Usuario convert() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setMatricula(this.matricula);
        return getUsuario(usuario, this.curriculos, this.arquivo, this.nome, this.email, this.senha, this.vagas);
    }
}
