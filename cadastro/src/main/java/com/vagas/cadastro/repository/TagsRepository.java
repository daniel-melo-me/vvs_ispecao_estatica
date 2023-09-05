package com.vagas.cadastro.repository;

import com.vagas.cadastro.config.request.TagsRequestDTO;
import com.vagas.cadastro.model.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {

    @Query(value = "SELECT t FROM Tags t " +
            "   WHERE t.nome LIKE CONCAT('%',:#{#filter.nome},'%') ")
    Page<Tags> findFilterList(TagsRequestDTO filter, Pageable pageable);

    boolean existsByNome(String nome);
}
