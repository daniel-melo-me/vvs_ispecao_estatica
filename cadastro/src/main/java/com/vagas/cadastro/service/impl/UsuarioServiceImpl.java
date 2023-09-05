package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.config.request.EmailRequestDTO;
import com.vagas.cadastro.config.request.UsuarioEditRequestDTO;
import com.vagas.cadastro.config.request.UsuarioRequestDTO;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Email;
import com.vagas.cadastro.model.Perfil;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import com.vagas.cadastro.repository.ArquivoRepository;
import com.vagas.cadastro.repository.PerfilRepository;
import com.vagas.cadastro.repository.UsuarioRepository;
import com.vagas.cadastro.service.EmailService;
import com.vagas.cadastro.service.UsuarioService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, EmailService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository repository;
    private final ArquivoRepository arquivoRepository;
    private final PasswordEncoder encoder;
    private final ArrayList<String> matriculasProfessor = new ArrayList<>();
    private final ArrayList<String> extensaoImagens = new ArrayList<>();
    private static final String[] EXTENCOES = {
            ".JPEG", ".jpeg", ".JFIF", ".jfif", ".BMP", ".bpm", ".PNG", ".png", "webp",
            ".PSD", ".psd", ".TIFF", ".tif", "EXIF", "exif", "RAW", "raw", "WEBP",
    };

    @Override
    public Page<Usuario> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Usuario editar(Long id, UsuarioEditRequestDTO dto) {
        verificarId(id);
        try {
            Usuario userMatricula = repository.getReferenceById(id);
            String matricula = userMatricula.getMatricula();
            if (repository.existsById(id)) {
                if (existsByEmail(dto.getEmail())) {
                    throw new RuntimeException("E-mail ja existente");
                }
                Usuario usuario = dto.convert();
                Usuario user = repository.findById(id).orElseThrow(
                        () -> new RuntimeException("Perfil não encontrado")
                );
                setarDTO(dto, usuario, user);
                setarCampos(usuario, user);
                usuario.setMatricula(matricula);
                usuario.setId(id);
                usuario.setPerfis(user.getPerfis());
                repository.save(usuario);
                return usuario;
            }
            throw new RuntimeException("erro desconhecido");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void setarDTO(UsuarioEditRequestDTO dto,
                          Usuario usuario,
                          Usuario user) {
        if (!isNull(dto.getArquivo())) {
            Arquivo arquivo = arquivoRepository.findById(dto.getArquivo().getId()).orElseThrow(
                    () -> new RuntimeException("Arquivo não encontrado")
            );
            usuario.setArquivo(arquivo);
        } else {
            usuario.setArquivo(user.getArquivo());
        }
        if (!isNull(dto.getSenha())) {
            usuario.setSenha(encoder.encode(dto.getSenha()));
        } else {
            usuario.setSenha(user.getSenha());
        }
    }

    private void setarCampos(Usuario usuario, Usuario user) {
        if (isNull(usuario.getSenha()) || isBlank(usuario.getSenha())) {
            usuario.setSenha(user.getSenha());
        }
        if (isNull(usuario.getEmail()) || isBlank(usuario.getEmail())) {
            usuario.setEmail(user.getEmail());
        }
        if (isNull(usuario.getNome()) || isBlank(usuario.getNome())) {
            usuario.setNome(user.getNome());
        }
    }

    public Boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public Boolean existsByMatricula(String matricula) {
        return repository.existsByMatricula(matricula);
    }

    @Override
    public void deletar(Long id) {
        verificarId(id);
        repository.deleteById(id);
    }

    @Override
    public Usuario pesquisar(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );
    }

    @Override
    public Page<Usuario> findPagedByFilters(UsuarioRequestDTO filter, Pageable pageable) {
        if (ObjectUtils.isEmpty(filter)) {
            throw new RuntimeException("Pelo Menos um filtro deve ser enviado");
        }

        return repository.findByMatriculaContainsOrNomeContainsOrEmailContains(
                filter.getMatricula(),
                filter.getNome(),
                filter.getEmail(),
                pageable);
    }

    @Override
    public void configurar() {
        Perfil admin = new Perfil();
        Perfil aluno = new Perfil();
        Perfil professor = new Perfil();
        setPerfil(professor, 3L, PerfilEnum.ROLE_PROFESSOR);
        setPerfil(aluno, 2L, PerfilEnum.ROLE_ALUNO);
        setPerfil(admin, 1L, PerfilEnum.ROLE_ADMIN);
        saveUsuarioMaster();
    }

    private void saveUsuarioMaster() {
        Usuario usuarioMaster = new Usuario();
        usuarioMaster.setMatricula("admin");
        usuarioMaster.setEmail("admin@gmail.com");
        usuarioMaster.setNome("admin");
        Perfil perfil = perfilRepository.findById(1L).orElseThrow(
                () -> new RuntimeException("Perfil não encontrado")
        );
        usuarioMaster.setPerfis(perfil);
        usuarioMaster.setDataCriacao(LocalDate.now());
        usuarioMaster.setSenha(encoder.encode("admin"));
        repository.save(usuarioMaster);
    }

    private void setPerfil(Perfil perfil, Long id, PerfilEnum perfilEnum) {
        perfil.setId(id);
        perfil.setNome(perfilEnum);
        perfilRepository.save(perfil);
    }


    private void setarPerfil(Long id, Usuario usuario) {
        Perfil perfil = new Perfil();
        if (id == 2) {
            perfil.setNome(PerfilEnum.ROLE_ALUNO);
        } else {
            perfil.setNome(PerfilEnum.ROLE_PROFESSOR);
        }
        perfil.setId(id);
        usuario.setPerfis(perfil);
    }

    @Override
    public Usuario salvarUsuario(UsuarioRequestDTO dto) {
        validarImagem(dto);
        validarCamposExistentes(dto);
        validarCampos(dto);
        Usuario usuario = dto.convert();
        if (!isNull(dto.getArquivo())) {
            Arquivo arquivo = retornarArquivo(dto);
            usuario.setArquivo(arquivo);
        }
        validarInformacoes(dto, usuario);
        usuario.setSenha(encoder.encode(dto.getSenha()));
        return repository.save(usuario);
    }

    private void validarImagem(UsuarioRequestDTO dto) {
        carregaExtensaoImagens();
        if (!isNull(dto.getArquivo())) {
            Arquivo arquivo = retornarArquivo(dto);
            int digito = arquivo.getFileName().lastIndexOf(".");
            String extencao = arquivo.getFileName().substring(arquivo.getFileName().length() - (arquivo.getFileName().length() - digito));
            if (!extensaoImagens.contains(extencao)) {
                throw new RuntimeException("Imagem com formato incorreto");
            }
        }
    }

    private Arquivo retornarArquivo(UsuarioRequestDTO dto) {
        return arquivoRepository.findById(dto.getArquivo().getId()).orElseThrow(
                () -> new RuntimeException("Arquivo não encontrado")
        );
    }

    private void validarCampos(UsuarioRequestDTO dto) {
        validarNuloECampoEmBranco(dto.getNome(), "nome");
        validarNuloECampoEmBranco(dto.getEmail(), "email");
        validarNuloECampoEmBranco(dto.getSenha(), "senha");
        validarNuloECampoEmBranco(dto.getMatricula(), "matricula");
    }

    private void validarNuloECampoEmBranco(String campo, String nome) {
        if (isNull(campo) || isBlank(campo)) {
            throw new RuntimeException("Campo " + nome + " obrigatório");
        }
    }

    private void validarInformacoes(UsuarioRequestDTO dto, Usuario usuario) {

        if (!isNull(dto.getArquivo())) {
            if (!arquivoRepository.existsById(dto.getArquivo().getId())) {
                throw new RuntimeException("Id do arquivo inválido");
            }
        }
        carregaMatriculas();
        if (matriculasProfessor.contains(dto.getMatricula())) {
            setarPerfil(3L, usuario);
        } else {
            setarPerfil(2L, usuario);
        }
    }

    private void validarCamposExistentes(UsuarioRequestDTO dto) {
        if (existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail ja existente");
        }
        if (existsByMatricula(dto.getMatricula())) {
            throw new RuntimeException("Matricula já existente");
        }
    }

    private void carregaExtensaoImagens() {
        extensaoImagens.addAll(List.of(EXTENCOES));
    }

    private void carregaMatriculas() {
        matriculasProfessor.add("10020030011");
        matriculasProfessor.add("10020030022");
        matriculasProfessor.add("10020030033");
    }

    private void verificarId(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }

    @Override
    @Builder
    public Email sendEmail(EmailRequestDTO dto) {
        return Email.
                builder()
                .emailTo(dto.getEmailTo())
                .text(dto.getText())
                .subject(dto.getSubject())
                .build();
    }
}
