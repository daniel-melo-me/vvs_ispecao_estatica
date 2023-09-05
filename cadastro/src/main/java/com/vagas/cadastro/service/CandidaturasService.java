package com.vagas.cadastro.service;

import com.vagas.cadastro.config.request.CandidaturasRequestDTO;
import com.vagas.cadastro.model.Candidaturas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidaturasService {

    Page<Candidaturas> listar(Pageable pageable);

    Candidaturas salvar(CandidaturasRequestDTO dto);

    void deletar(Long id);

    Candidaturas pesquisar(Long id);

}
