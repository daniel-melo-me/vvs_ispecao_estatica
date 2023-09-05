package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.config.request.CurriculoRequestDTO;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Tags;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.repository.ArquivoRepository;
import com.vagas.cadastro.repository.CurriculoRepository;
import com.vagas.cadastro.repository.TagsRepository;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.service.CurriculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class CurriculoServiceImpl implements CurriculoService {

    private final TagsRepository tagsRepository;
    private final CurriculoRepository repository;
    private final ArquivoRepository arquivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ArrayList<String> extensaoImagens = new ArrayList<>();
    private static final String[] EXTENCOES = {
            ".TXT", ".txt", ".PDF", ".pdf", ".DOC", ".doc", ".DOCX", ".docx",
            ".ppt", ".PPT", ".pps", ".PPS"
    };

    @Override
    public Page<Curriculo> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Curriculo salvar(CurriculoRequestDTO dto, Long idUser) {
        validarCamposObrigatorios(dto);
        validarArquivo(dto);
        validaExtencaoImagem(dto);
        Curriculo curriculo = dto.convert();
        Usuario usuario = usuarioRepository.findById(idUser).orElseThrow();
        List<Tags> tags = validarTag(curriculo);
        Arquivo arquivo = arquivoRepository.findById(dto.getArquivo().getId()).orElseThrow(
                () -> new RuntimeException("Arquivo não encontrado")
        );
        curriculo.setUsuario(usuario);
        curriculo.setArquivo(arquivo);
        curriculo.setTags(tags);
        return repository.save(curriculo);
    }

    private void validarArquivo(CurriculoRequestDTO dto) {
        if (!isNull(dto.getArquivo())) {
            arquivoRepository.findById(dto.getArquivo().getId()).orElseThrow(
                    () -> new RuntimeException("Arquivo não encontrado")
            );
        }
        if (isNull(dto.getArquivo())) {
            throw new RuntimeException("Arquivo não encontrado");
        }
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Curriculo pesquisar(Long id) {
        if (repository.existsById(id)) {
            return repository.findById(id).orElseThrow();
        }
        throw new RuntimeException("Curriculo não encontrado");
    }

    @Override
    public Page<Curriculo> findPagedByFilters(CurriculoRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }
        return repository.findFilterList(filter, pageable);
    }

    @Override
    public Curriculo editar(Long id, CurriculoRequestDTO dto, Long idUser) {
        verificarId(id);
        validarCamposObrigatorios(dto);
        validarArquivoCasoExista(dto);
        Curriculo curriculo = dto.convert();
        Curriculo newCurriculo = repository.findById(id).orElseThrow();
        validarTag(curriculo);
        curriculo.setId(id);
        curriculo.setUsuario(newCurriculo.getUsuario());
        return repository.save(curriculo);
    }

    public void validarArquivoCasoExista(CurriculoRequestDTO dto) {
        if (!isNull(dto.getArquivo())) {
            arquivoRepository.findById(dto.getArquivo().getId()).orElseThrow(()
                    -> new RuntimeException("Arquivo não encontrado"));
        }
    }

    public void validaExtencaoImagem(CurriculoRequestDTO dto) {
        carregaExtensaoImagens();

        Arquivo arquivo = arquivoRepository.findById(dto.getArquivo().getId()).orElseThrow(
                () -> new RuntimeException("Arquivo não encontrado")
        );

        int digito = arquivo.getFileName().lastIndexOf(".");
        String extencao = arquivo.getFileName().substring(arquivo.getFileName().length() - (arquivo.getFileName().length() - digito));

        if (!extensaoImagens.contains(extencao)) {
            throw new RuntimeException("formato de documento inválido");
        }
    }

    public void carregaExtensaoImagens() {
        extensaoImagens.addAll(List.of(EXTENCOES));
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }

    private void validarCamposObrigatorios(CurriculoRequestDTO dto) {
        if (isNull(dto.getTitulo()) || isBlank(dto.getTitulo())) {
            throw new RuntimeException("Campo titulo obrigatório");
        }
    }

    private List<Tags> validarTag(Curriculo curriculo) {
        List<Tags> tags = new ArrayList<>();
        if (!isNull(curriculo.getTags())) {
            for (Tags tag : curriculo.getTags()) {
                if (!isNull(tag.getId())) {
                    Tags newTag = tagsRepository.findById(tag.getId()).orElseThrow(
                            () -> new RuntimeException("Tag não existente")
                    );
                    tags.add(newTag);
                }
            }
        }
        return tags;
    }
}
