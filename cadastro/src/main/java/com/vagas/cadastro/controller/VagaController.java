package com.vagas.cadastro.controller;

import com.vagas.cadastro.config.request.StatusRequestDTO;
import com.vagas.cadastro.config.request.VagaRequestDTO;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.repository.VagaRepository;
import com.vagas.cadastro.service.TokenService;
import com.vagas.cadastro.service.VagaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/vaga")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class VagaController {

    private final TokenService tokenService;
    private final VagaService service;
    private final Map<String, String> retorno = new HashMap<>();
    private final Map<String, String> erro = new HashMap<>();
    private final UsuarioRepository repository;
    private final VagaRepository vagaRepository;

    @PostMapping("/criar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> salvar(@RequestBody @Valid VagaRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(service.salvar(dto, tokenService.returnId()));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        retorno.put("response", "Vaga deletada com sucesso!");
        try {
            if (!verificacaoDePermissaoPeloId(id)) {
                return ResponseEntity.status(401).build();
            }
            service.deletar(id);
            return ResponseEntity.ok().body(retorno);
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/pesquisar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    public ResponseEntity<?> pesquisar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.pesquisar(id));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> findPagedByFilter(
            @RequestBody @Valid VagaRequestDTO filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.findPagedByFilters(filter, pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    public ResponseEntity<?> listar(Pageable pageable) {
        try {
            return ResponseEntity.ok().body(service.listar(pageable));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid VagaRequestDTO dto) {
        try {
            if (!verificacaoDePermissaoPeloId(id)) {
                return ResponseEntity.status(401).build();
            }
            return ResponseEntity.ok().body(service.editar(id, dto, tokenService.returnId()));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> status(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid StatusRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(service.status(id, dto));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    private ResponseEntity<?> retornoErro(String mensagem) {
        erro.put("erro", mensagem);
        log.error("Erro encontrado: " + mensagem);
        return ResponseEntity.badRequest().body(erro);
    }

    private boolean verificacaoDePermissaoPeloId(Long id) {
        Usuario usuario = repository.findById(tokenService.returnId()).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );
        List<Vaga> vagas = vagaRepository.findAll();
        if (usuario.getPerfis().getNome().equals(PerfilEnum.ROLE_ADMIN)) {
            return true;
        }
        for (Vaga vaga : vagas) {
            if (vaga.getUsuario().getId().equals(usuario.getId()) && vaga.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
