package com.vagas.cadastro.service;

import com.vagas.cadastro.config.request.CurriculoRequestDTO;
import com.vagas.cadastro.model.Curriculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurriculoService {

    Page<Curriculo> listar(Pageable pageable);

    Curriculo salvar(CurriculoRequestDTO dto, Long idUser);

    void deletar(Long id);

    Curriculo pesquisar(Long id);

    Page<Curriculo> findPagedByFilters(CurriculoRequestDTO filter, Pageable pageable);

    Curriculo editar(Long id, CurriculoRequestDTO dto, Long idUser);

}
