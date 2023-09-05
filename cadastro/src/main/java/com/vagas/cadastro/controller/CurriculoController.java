package com.vagas.cadastro.controller;

import com.vagas.cadastro.config.request.CurriculoRequestDTO;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import com.vagas.cadastro.repository.CurriculoRepository;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.service.CurriculoService;
import com.vagas.cadastro.service.TokenService;
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
@RequestMapping(path = "/curriculo")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CurriculoController {

    private final CurriculoService service;
    private final Map<String, String> retorno = new HashMap<>();
    private final Map<String, String> erro = new HashMap<>();
    private final TokenService tokenService;
    private final UsuarioRepository repository;
    private final CurriculoRepository curriculoRepository;

    @PostMapping("/criar")
    @PreAuthorize("hasRole('ALUNO') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> salvar(@RequestBody @Valid CurriculoRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(service.salvar(dto, tokenService.returnId()));
        } catch (Exception e) {
            return retornoErro(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('ADMIN') or hasRole('ALUNO')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        retorno.put("response", "Currículo deletado com sucesso!");
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
            @RequestBody @Valid CurriculoRequestDTO filter,
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO')")
    @Transactional
    public ResponseEntity<?> editar(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid CurriculoRequestDTO dto) {
        try {
            if (!verificacaoDePermissaoPeloId(id)) {
                return ResponseEntity.status(401).build();
            }
            return ResponseEntity.ok().body(service.editar(id, dto, tokenService.returnId()));
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
        List<Curriculo> curriculos = curriculoRepository.findAll();
        if (!usuario.getPerfis().getNome().equals(PerfilEnum.ROLE_ADMIN)) {
            for (Curriculo curriculo : curriculos) {
                if (curriculo.getUsuario().getId().equals(usuario.getId()) && curriculo.getId().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }
}
