package com.vagas.cadastro.controller;

import com.vagas.cadastro.config.request.CandidaturasRequestDTO;
import com.vagas.cadastro.service.CandidaturasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/candidaturas")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CandidaturasController {

    private final CandidaturasService service;
    private final Map<String, String> erro = new HashMap<>();
    private final Map<String, String> retorno = new HashMap<>();

    @PostMapping("/criar")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> criar(@RequestBody @Valid CandidaturasRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(service.salvar(dto));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR') or hasRole('ALUNO')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        retorno.put("response", "Candidatura deletada com sucesso!");
        try {
            service.deletar(id);
            return ResponseEntity.ok().body(retorno);
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/pesquisar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO') ")
    public ResponseEntity<?> pesquisar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.pesquisar(id));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO') ")
    public ResponseEntity<?> listar(Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.listar(pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    private ResponseEntity<?> retornoErro(String mensagem) {
        erro.put("erro", mensagem);
        log.error("Erro encontrado: " + mensagem);
        return ResponseEntity.badRequest().body(erro);
    }
}
