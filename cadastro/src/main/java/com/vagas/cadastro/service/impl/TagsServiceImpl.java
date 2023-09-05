package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.config.request.TagsRequestDTO;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.repository.TagsRepository;
import com.vagas.cadastro.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {

    private final TagsRepository repository;

    @Override
    public Page<Tags> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Tags salvar(TagsRequestDTO dto) {
        validarCampos(dto);
        Tags tags = dto.convert();
        return repository.save(tags);
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Tags pesquisar(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Tag não encontrada")
        );
    }

    @Override
    public Page<Tags> findPagedByFilters(TagsRequestDTO filter, Pageable pageable) {
        return repository.findFilterList(filter, pageable);
    }

    @Override
    public Tags editar(Long id, TagsRequestDTO dto) {
        if (repository.existsById(id)) {
            validarCampos(dto);
            Tags tags = dto.convert();
            tags.setId(id);
            return repository.save(tags);
        }
        throw new RuntimeException("Id não encontrado");
    }

    private void validarCampos(TagsRequestDTO dto) {
        if (isNull(dto.getNome()) || isBlank(dto.getNome())) {
            throw new RuntimeException("Campo nome obrigatório");
        }
        if (repository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Campo nome já existente");
        }
        validarCampoExistente(dto);
    }

    private void validarCampoExistente(TagsRequestDTO dto) {
        if (existByNome(dto.getNome())) {
            throw new RuntimeException("Nome de Tag já existente");
        }
    }

    private boolean existByNome(String nome) {
        return repository.existsByNome(nome);
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }
}
