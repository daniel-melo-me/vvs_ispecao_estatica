package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.config.request.StatusRequestDTO;
import com.vagas.cadastro.config.request.VagaRequestDTO;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.model.enumeration.StatusEnum;
import com.vagas.cadastro.repository.TagsRepository;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.repository.VagaRepository;
import com.vagas.cadastro.service.VagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class VagaServiceImpl implements VagaService {

    private final VagaRepository repository;
    private final TagsRepository tagsRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Page<Vaga> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Vaga salvar(VagaRequestDTO dto, Long idUser) {
        validar(dto);
        validarData(dto.getExpiracao());
        Vaga vaga = dto.convert();
        validarTag(vaga);
        vaga.setStatus(StatusEnum.ABERTA);
        Usuario usuario = usuarioRepository.findById(idUser).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );
        vaga.setUsuario(usuario);
        adicionarTags(dto, vaga);
        return repository.save(vaga);
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Vaga pesquisar(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Vaga não encontrada")
        );
    }

    @Override
    public Page<Vaga> findPagedByFilters(VagaRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }
        return repository.findByTituloContainsOrDescricaoContains(
                filter.getTitulo(),
                filter.getDescricao(),
                pageable
        );
    }

    @Override
    public Vaga editar(Long id, VagaRequestDTO dto, Long idUser) {
        verificarId(id);
        Vaga vagaTituloIgual = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Vaga não encontrada")
        );

        if (repository.existsByTitulo(dto.getTitulo())  &&
                !(dto.getTitulo().equalsIgnoreCase(vagaTituloIgual.getTitulo()))) {
            throw new RuntimeException("Título já existente");
        }

        validarData(dto.getExpiracao());
        Vaga vaga = dto.convert();
        Usuario usuario = usuarioRepository.findById(idUser).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );
        validarTag(vaga);
        vaga.setId(id);
        vaga.setUsuario(usuario);
        return repository.save(vaga);
    }

    @Override
    public Vaga status(Long id, StatusRequestDTO dto) {
        Vaga vaga = repository.findById(id).orElseThrow(
                () -> new RuntimeException("vaga não encontrada")
        );
        vaga.setStatus(dto.getStatus());
        return vaga;
    }

    public void validar(VagaRequestDTO dto) {
        validarCampoNuloECampoEmBranco(dto.getTitulo(), "titulo");
        validarCampoNuloECampoEmBranco(dto.getDescricao(), "descricao");
        if (isNull(dto.getInstitucional())) {
            throw new RuntimeException("Campo institucional obrigatório");
        }
        if (repository.existsByTitulo(dto.getTitulo())) {
            throw new RuntimeException("Título já existente");
        }
    }

    private void validarCampoNuloECampoEmBranco(String campo, String nome) {
        if (isNull(campo) || isBlank(campo)) {
            throw new RuntimeException("Campo " + nome + " obrigatório");
        }
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }

    private void validarData(LocalDateTime data) {
        LocalDateTime dataAtual = LocalDateTime.now();
        if (!data.isAfter(dataAtual)) {
            throw new RuntimeException("Data de expiração já passou");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime.parse(data.format(formatter), formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválida");
        }
    }

    private void adicionarTags(VagaRequestDTO dto, Vaga vaga) {
        List<Tags> tagsList = dto.getTags();
        List<Tags> addTags = new ArrayList<>();
        for (Tags tag : tagsList) {
            Tags tags = tagsRepository.findById(tag.getId()).orElseThrow(
                    () -> new RuntimeException("Tag não encontrada")
            );
            addTags.add(tags);
        }
        vaga.setTags(addTags);
    }

    private void validarTag(Vaga vaga) {
        if (!isNull(vaga.getTags())) {
            for (Tags tags : vaga.getTags()) {
                if ((!isNull(tags.getId())) && (!tagsRepository.existsById(tags.getId()))) {
                    throw new RuntimeException("Tag não existente");
                }
            }
        }
    }
}
