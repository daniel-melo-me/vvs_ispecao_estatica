package com.vagas.cadastro.repository;

import com.vagas.cadastro.config.request.CurriculoRequestDTO;
import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.model.Curriculo;
import com.vagas.cadastro.model.Perfil;
import com.vagas.cadastro.model.Usuario;
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

import java.util.concurrent.atomic.AtomicInteger;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CurriculoRepositoryTest {

    @Autowired
    private CurriculoRepository repository;
    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    private Curriculo curriculo;

    @BeforeEach
    public void setup() {
        curriculo = new Curriculo();
        curriculo.setTitulo("titulo principal");
        curriculo.setDescricao("teste");
        Arquivo arquivo = new Arquivo();
        arquivo.setFileName("Manual RNServiceVirtualization.pdf");
        arquivo.setFileType("application/pdf");
        byte[] data = new byte[5];
        arquivo.setData(data);
        arquivoRepository.save(arquivo);
        curriculo.setArquivo(arquivo);

        Usuario usuario = new Usuario();
        usuario.setMatricula("20123213");
        usuario.setSenha("324234");
        usuario.setEmail("afsdfsad@gmail.com");
        usuario.setNome("junior");

        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        perfilRepository.save(perfil);

        usuario.setPerfis(perfil);
        Arquivo imagem = new Arquivo();
        imagem.setFileName("Service_Virtualization_Vetor.png");
        imagem.setFileType("image/png");
        byte[] data2 = new byte[5];
        imagem.setData(data2);
        arquivoRepository.save(imagem);
        usuario.setArquivo(imagem);
        usuarioRepository.save(usuario);

        curriculo.setUsuario(usuario);
        repository.save(curriculo);
    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
        arquivoRepository.deleteAll();
        perfilRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    public void deveCriarUmCurriculoEVerificarPeloId() {
        setup();
        Assert.assertTrue(repository.existsById(curriculo.getId()));
    }

    @Test
    public void deveDeletarUmCurriculo() {
        setup();
        repository.delete(curriculo);

        Iterable<Curriculo> all = repository.findAll();
        AtomicInteger counter = new AtomicInteger();
        all.forEach(it -> counter.getAndIncrement());

        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void deveEditarUmCurriculo() {
        setup();
        repository.save(curriculo);
        curriculo.setDescricao("nova descrição");

        Assert.assertEquals("nova descrição", curriculo.getDescricao());
    }

    @Test
    public void deveRetornarUmaListaDeCurriculos() {
        setup();
        Assert.assertNotNull(repository.findAll());
    }

    @Test
    public void deveFiltrarUmCurriculoERetornarUmaLista() {
        setup();
        Assert.assertNotNull(repository.findFilterList(new CurriculoRequestDTO(curriculo), null));
    }
}
