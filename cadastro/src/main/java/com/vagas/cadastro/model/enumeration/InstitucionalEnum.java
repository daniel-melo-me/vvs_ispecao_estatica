package com.vagas.cadastro.model.enumeration;

public enum InstitucionalEnum {
    INTERNO("Interno"),
    EXTERNO("Externo");

    private final String descricao;

    InstitucionalEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
