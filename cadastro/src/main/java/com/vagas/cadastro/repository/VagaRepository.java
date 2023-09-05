package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Vaga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    Page<Vaga> findByTituloContainsOrDescricaoContains(
            String titulo,
            String descricao,
            Pageable pageable);

    boolean existsByTitulo(String titulo);

}

