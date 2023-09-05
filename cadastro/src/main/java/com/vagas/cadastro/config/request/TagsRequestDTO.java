package com.vagas.cadastro.config.request;

import com.sun.istack.NotNull;
import com.vagas.cadastro.model.Tags;
import lombok.Data;

@Data
public class TagsRequestDTO {

    private Long id;
    @NotNull
    private String nome;

    public TagsRequestDTO() {
    }

    public TagsRequestDTO(Tags tags) {
        this.id = tags.getId();
        this.nome = tags.getNome();
    }

    public Tags convert() {
        Tags tags = new Tags();
        tags.setId(this.id);
        tags.setNome(this.nome);
        return tags;
    }
}
