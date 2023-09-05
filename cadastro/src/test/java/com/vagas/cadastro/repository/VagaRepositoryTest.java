package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Perfil;
import com.vagas.cadastro.model.Usuario;
import com.vagas.cadastro.model.Vaga;
import com.vagas.cadastro.model.enumeration.InstitucionalEnum;
import com.vagas.cadastro.model.enumeration.PerfilEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class VagaRepositoryTest {

    @Autowired
    private VagaRepository repository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    private Vaga vaga;

    @BeforeEach
    public void setup() {
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
        vaga.setUsuario(usuario);
        vaga.setSalario("100,00");
        repository.save(vaga);
    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
        perfilRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Esse Teste tem como objetivo criar uma vaga e após isso verificar pelo seu id")
    public void deveCriarUmaVagaVerificandoPeloId() {
        setup();
        Assert.assertTrue(repository.existsById(vaga.getId()));
    }

    @Test
    public void deveDeletarUmaVaga() {
        setup();
        repository.delete(vaga);

        Iterable<Vaga> all = repository.findAll();
        AtomicInteger counter = new AtomicInteger();
        all.forEach(it -> counter.getAndIncrement());

        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void deveEditarUmaVaga() {
        setup();
        vaga.setTitulo("novoTitulo");
        Assert.assertEquals("novoTitulo", vaga.getTitulo());
    }

    @Test
    public void deveRetornarUmaListaDeVagas() {
        setup();
        Assert.assertNotNull(repository.findAll());
    }

    @Test
    public void deveFiltrarERetornarUmaListaDeVagas() {
        setup();
        Assert.assertNotNull(repository.findByTituloContainsOrDescricaoContains(
                vaga.getTitulo(),
                vaga.getDescricao(),
                null));
    }
}
