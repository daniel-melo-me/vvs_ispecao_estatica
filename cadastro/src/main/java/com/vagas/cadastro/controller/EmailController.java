package com.vagas.cadastro.controller;

import com.vagas.cadastro.config.request.EmailRequestDTO;
import com.vagas.cadastro.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class EmailController {

    private final EmailService service;
    private final Map<String, String> erro = new HashMap<>();

    @PostMapping("/enviar")
    @Transactional
    public ResponseEntity<?> enviar(@RequestBody @Valid EmailRequestDTO dto) {
        try {
            return ResponseEntity.status(201).body(service.sendEmail(dto));
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
