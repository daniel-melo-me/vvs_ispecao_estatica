package com.vagas.cadastro.config.request;

import com.sun.istack.NotNull;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.model.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class CurriculoRequestDTO {

    private Long id;
    private String descricao;
    @NotNull
    private Arquivo arquivo;
    private Usuario usuario;
    private List<Tags> tags;
    @NotNull
    private String titulo;

    public CurriculoRequestDTO() {
    }

    public CurriculoRequestDTO(Curriculo curriculo) {
        this.id = curriculo.getId();
        this.descricao = curriculo.getDescricao();
        this.arquivo = curriculo.getArquivo();
        this.usuario = curriculo.getUsuario();
        this.tags = curriculo.getTags();
        this.titulo = curriculo.getTitulo();
    }

    public Curriculo convert() {
        Curriculo curriculo = new Curriculo();
        curriculo.setId(this.id);
        curriculo.setDescricao(this.descricao);
        curriculo.setArquivo(this.arquivo);
        curriculo.setUsuario(this.usuario);
        curriculo.setTags(this.tags);
        curriculo.setTitulo(this.titulo);
        return curriculo;
    }
}
