package com.vagas.cadastro.model.enumeration;

public enum PerfilEnum {
    ROLE_ADMIN("Admin"),
    ROLE_ALUNO("Aluno"),
    ROLE_PROFESSOR("Professor");

    private final String descricao;

    PerfilEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
