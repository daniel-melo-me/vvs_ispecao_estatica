package com.vagas.cadastro.config.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.model.enumeration.InstitucionalEnum;
import com.vagas.cadastro.model.enumeration.StatusEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VagaRequestDTO {

    private Long id;
    @NotNull
    private String titulo;
    @NotNull
    private String descricao;
    private String link;
    private String salario;
    @NotNull
    private InstitucionalEnum institucional;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime expiracao;
    private StatusEnum status;
    private Usuario usuario;
    private List<Tags> tags;

    public VagaRequestDTO() {
    }

    public VagaRequestDTO(Vaga vaga) {
        this.id = vaga.getId();
        this.titulo = vaga.getTitulo();
        this.descricao = vaga.getDescricao();
        this.link = vaga.getLink();
        this.institucional = vaga.getInstitucional();
        this.expiracao = vaga.getExpiracao();
        this.status = vaga.getStatus();
        this.usuario = vaga.getUsuario();
        this.tags = vaga.getTags();
        this.salario = vaga.getSalario();
    }

    public Vaga convert() {
        Vaga vaga = new Vaga();
        vaga.setId(this.id);
        vaga.setTitulo(this.titulo);
        vaga.setDescricao(this.descricao);
        vaga.setLink(this.link);
        vaga.setInstitucional(this.institucional);
        vaga.setExpiracao(this.expiracao);
        vaga.setStatus(this.status);
        vaga.setUsuario(this.usuario);
        vaga.setTags(this.tags);
        vaga.setSalario(this.salario);
        return vaga;
    }
}
