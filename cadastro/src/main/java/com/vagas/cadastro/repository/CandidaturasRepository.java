package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Candidaturas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidaturasRepository extends JpaRepository<Candidaturas, Long> {
}
