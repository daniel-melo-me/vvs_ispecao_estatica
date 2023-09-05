package com.vagas.cadastro.config.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.Vaga;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Data
public class UsuarioEditRequestDTO {

    private PasswordEncoder encoder;

    private Long id;
    private List<Curriculo> curriculos;
    private Arquivo arquivo;
    private String nome;
    private String email;
    private String senha;
    @JsonIgnore
    private List<Vaga> vagas;

    public UsuarioEditRequestDTO() {
    }

    public UsuarioEditRequestDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.curriculos = usuario.getCurriculos();
        this.arquivo = usuario.getArquivo();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = encoder.encode(usuario.getSenha());
        this.vagas = usuario.getVagas();
    }

    public Usuario convert() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        return getUsuario(usuario, this.curriculos, this.arquivo, this.nome, this.email, this.senha, this.vagas);
    }

    static Usuario getUsuario(Usuario usuario, List<Curriculo> curriculos, Arquivo arquivo, String nome, String email, String senha, List<Vaga> vagas) {
        usuario.setCurriculos(curriculos);
        usuario.setArquivo(arquivo);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setVagas(vagas);
        return usuario;
    }
}
