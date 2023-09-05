package com.vagas.cadastro.model.enumeration;

public enum StatusEnum {
    FINALIZADA("Finalizada"),
    SUSPENSA("Suspensa"),
    ABERTA("Aberta");

    private final String descricao;

    StatusEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
