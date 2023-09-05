package com.vagas.cadastro.repository;

import com.vagas.cadastro.config.request.CurriculoRequestDTO;
import com.vagas.cadastro.model.Curriculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculoRepository extends JpaRepository<Curriculo, Long> {

    @Query(value = "SELECT c FROM Curriculo c " +
            "   WHERE c.descricao LIKE CONCAT('%',:#{#filter.descricao},'%') ")
    Page<Curriculo> findFilterList(CurriculoRequestDTO filter, Pageable pageable);

}
