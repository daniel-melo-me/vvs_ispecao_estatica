package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Arquivo;
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
public class ArquivoRepositoryTest {

    @Autowired
    private ArquivoRepository repository;
    private Arquivo arquivo;

    @BeforeEach
    public void setup() {
        arquivo = new Arquivo();
        arquivo.setFileName("Manual RNServiceVirtualization.pdf");
        arquivo.setFileType("application/pdf");
        byte[] data = new byte[5];
        arquivo.setData(data);
        repository.save(arquivo);
    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    public void deveSalvarArquivoERetornarUmId() {
        setup();
        Assert.assertTrue(repository.existsById(arquivo.getId()));
    }

    @Test
    public void deveDeletarUmArquivo() {
        setup();
        repository.delete(arquivo);

        Iterable<Arquivo> all = repository.findAll();
        AtomicInteger counter = new AtomicInteger();
        all.forEach(it -> counter.getAndIncrement());

        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void deveEditarUmArquivo() {
        setup();
        byte[] newData = new byte[4];
        arquivo.setData(newData);

        Assert.assertEquals(newData, arquivo.getData());
    }

    @Test
    public void deveRetornarUmArquivo() {
        setup();
        Assert.assertEquals(arquivo, repository.findById(arquivo.getId()).orElse(null));
    }
}
