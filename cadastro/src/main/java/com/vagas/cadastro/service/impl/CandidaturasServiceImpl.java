package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.config.request.CandidaturasRequestDTO;
import com.vagas.cadastro.model.Candidaturas;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.model.enumeration.StatusEnum;
import com.vagas.cadastro.repository.CandidaturasRepository;
import com.vagas.cadastro.repository.CurriculoRepository;
import com.vagas.cadastro.repository.VagaRepository;
import com.vagas.cadastro.service.CandidaturasService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CandidaturasServiceImpl implements CandidaturasService {

    private final CandidaturasRepository repository;
    private final VagaRepository vagaRepository;
    private final CurriculoRepository curriculoRepository;

    @Override
    public Page<Candidaturas> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Candidaturas salvar(CandidaturasRequestDTO dto) {
        validarCampos(dto);
        Candidaturas candidaturas = dto.convert();
        validarInformacoes(candidaturas);
        Vaga vaga = vagaRepository.findById(dto.getVaga().getId()).orElseThrow(
                () -> new RuntimeException("Vaga não encontrada")
        );
        if (vaga.getStatus().equals(StatusEnum.FINALIZADA)) {
            throw new RuntimeException("Não é possivel se candidatar em uma vaga finalizada");
        }
        Curriculo curriculo = curriculoRepository.findById(dto.getCurriculo().getId()).orElseThrow();
        candidaturas.setCurriculo(curriculo);
        candidaturas.setVaga(vaga);
        return repository.save(candidaturas);
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Candidaturas pesquisar(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Candidatura não encontrada")
        );
    }

    private void validarCampos(CandidaturasRequestDTO dto) {
        validarObjetoNulo(dto.getCurriculo(), "curriculo");
        validarObjetoNulo(dto.getVaga(), "vaga");
    }

    private void validarObjetoNulo(Object objeto, String campo) {
        if (isNull(objeto)) {
            throw new RuntimeException("campo " + campo + " é obrigatório");
        }
    }

    private void validarInformacoes(Candidaturas candidaturas) {
        if (!vagaRepository.existsById(candidaturas.getVaga().getId())) {
            throw new RuntimeException("vaga não existente");
        }
        if (!curriculoRepository.existsById(candidaturas.getCurriculo().getId())) {
            throw new RuntimeException("curriculo não existente");
        }
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }
}
