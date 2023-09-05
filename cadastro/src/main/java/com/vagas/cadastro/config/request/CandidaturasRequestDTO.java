package com.vagas.cadastro.config.request;

import com.sun.istack.NotNull;
import com.vagas.cadastro.model.Candidaturas;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Vaga;
import lombok.Data;

@Data
public class CandidaturasRequestDTO {

    private Long id;
    @NotNull
    private Vaga vaga;
    @NotNull
    private Curriculo curriculo;

    public CandidaturasRequestDTO() {
    }

    public CandidaturasRequestDTO(Candidaturas candidaturas) {
        this.id = candidaturas.getId();
        this.vaga = candidaturas.getVaga();
        this.curriculo = candidaturas.getCurriculo();
    }

    public Candidaturas convert() {
        Candidaturas candidaturas = new Candidaturas();
        candidaturas.setId(this.id);
        candidaturas.setVaga(this.vaga);
        candidaturas.setCurriculo(this.curriculo);
        return candidaturas;
    }
}
