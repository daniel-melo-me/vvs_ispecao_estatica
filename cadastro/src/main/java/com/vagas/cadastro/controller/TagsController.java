package com.vagas.cadastro.controller;

import com.vagas.cadastro.config.request.TagsRequestDTO;
import com.vagas.cadastro.service.TagsService;
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
@RequestMapping("/tags")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class TagsController {

    private final TagsService service;
    private final Map<String, String> retorno = new HashMap<>();
    private final Map<String, String> erro = new HashMap<>();

    @PostMapping("/criar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> criar(@RequestBody @Valid TagsRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(service.salvar(dto));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        retorno.put("response", "Tag deletada com sucesso!");
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

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO') ")
    @Transactional
    public ResponseEntity<?> findPagedByFilter(
            @RequestBody @Valid TagsRequestDTO filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.findPagedByFilters(filter, pageable));
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

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid TagsRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(service.editar(id, dto));
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
