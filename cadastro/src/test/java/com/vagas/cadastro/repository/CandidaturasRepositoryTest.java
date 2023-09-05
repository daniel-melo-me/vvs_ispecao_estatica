package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.*;
import com.vagas.cadastro.model.enumeration.InstitucionalEnum;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CandidaturasRepositoryTest {

    @Autowired
    private CandidaturasRepository repository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private CurriculoRepository curriculoRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;
    private Candidaturas candidaturas;
    private Vaga vaga;

    @BeforeEach
    public void setup() {
        candidaturas = new Candidaturas();

        Usuario usuario = new Usuario();
        usuario.setMatricula("20123213");
        usuario.setSenha("324234");
        usuario.setEmail("afsdfsad@gmail.com");
        usuario.setNome("junior");

        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        usuario.setPerfis(perfil);

        perfilRepository.save(perfil);
        usuarioRepository.save(usuario);

        vaga = new Vaga();
        vaga.setTitulo("teste criar vaga");
        vaga.setInstitucional(InstitucionalEnum.EXTERNO);
        vaga.setDescricao("Descrição da vaga");
        vaga.setExpiracao(LocalDateTime.of(2023, 12, 12, 12, 23));
        vaga.setLink("www.teste.com.br");
        vaga.setSalario("100,00");
        vaga.setUsuario(usuario);
        vagaRepository.save(vaga);

        Curriculo curriculo = new Curriculo();
        curriculo.setDescricao("teste");
        curriculo.setTitulo("titulo principal");
        Arquivo arquivo = new Arquivo();
        arquivo.setFileName("Manual RNServiceVirtualization.pdf");
        arquivo.setFileType("application/pdf");
        byte[] data = new byte[5];
        arquivo.setData(data);
        arquivoRepository.save(arquivo);
        curriculo.setArquivo(arquivo);
        curriculo.setUsuario(usuario);
        curriculoRepository.save(curriculo);

        candidaturas.setCurriculo(curriculo);
        candidaturas.setVaga(vaga);
        repository.save(candidaturas);
    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
        perfilRepository.deleteAll();
        usuarioRepository.deleteAll();
        vagaRepository.deleteAll();
        curriculoRepository.deleteAll();
        arquivoRepository.deleteAll();
    }

    @Test
    public void deveCriarUmaCandidaturaERetornarUmId() {
        setup();
        Assert.assertTrue(repository.existsById(candidaturas.getId()));
    }

    @Test
    public void deveDeletarUmaCandidatura() {
        setup();
        repository.delete(candidaturas);

        Iterable<Candidaturas> all = repository.findAll();
        AtomicInteger counter = new AtomicInteger();
        all.forEach(it -> counter.getAndIncrement());

        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void deveEditarUmaCandidatura() {
        setup();

        Vaga newVaga = new Vaga();
        vaga.setTitulo("new teste criar vaga");
        vaga.setInstitucional(InstitucionalEnum.EXTERNO);
        vaga.setDescricao("Descrição da vaga");
        vaga.setExpiracao(LocalDateTime.of(2023, 12, 12, 12, 23));
        vaga.setLink("www.teste.com.br");
        candidaturas.setVaga(newVaga);

        Assert.assertEquals(newVaga, candidaturas.getVaga());
    }

    @Test
    public void deveRetornarUmaCandidatura() {
        setup();
        Assert.assertEquals(candidaturas, repository.findById(candidaturas.getId()).orElse(null));
    }

    @Test
    public void deveRetornarUmaListaDeCandidaturas() {
        setup();
        Assert.assertNotNull(repository.findAll());
    }
}
