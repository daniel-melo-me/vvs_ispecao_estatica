package com.vagas.cadastro.service;

import com.vagas.cadastro.config.request.TagsRequestDTO;
import com.vagas.cadastro.model.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagsService {

    Page<Tags> listar(Pageable pageable);

    Tags salvar(TagsRequestDTO dto);

    void deletar(Long id);

    Tags pesquisar(Long id);

    Page<Tags> findPagedByFilters(TagsRequestDTO filter, Pageable pageable);

    Tags editar(Long id, TagsRequestDTO dto);

}
