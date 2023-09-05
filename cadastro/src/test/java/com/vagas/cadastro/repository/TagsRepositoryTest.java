package com.vagas.cadastro.repository;

import com.vagas.cadastro.config.request.TagsRequestDTO;
import com.vagas.cadastro.model.Tags;
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
public class TagsRepositoryTest {

    @Autowired
    private TagsRepository repository;
    private Tags tags;

    @BeforeEach
    public void setup() {
        tags = new Tags();
        tags.setNome("administração");
        repository.save(tags);
    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    public void deveCriarUmaTagEVerificarPeloId() {
        setup();
        Assert.assertTrue(repository.existsById(tags.getId()));
    }

    @Test
    public void deveDeletarUmaTag() {
        setup();
        repository.delete(tags);

        Iterable<Tags> all = repository.findAll();
        AtomicInteger counter = new AtomicInteger();
        all.forEach(it -> counter.getAndIncrement());

        Assert.assertEquals(0, counter.get());
    }

    @Test
    public void deveEditarUmaTag() {
        setup();
        tags.setNome("Tecnologia");

        Assert.assertEquals("Tecnologia", tags.getNome());
    }

    @Test
    public void deveRetornarUmaListaDeTags() {
        setup();
        Tags tags2 = new Tags();
        tags2.setNome("ciencia");
        repository.save(tags2);

        Assert.assertNotNull(repository.findAll());
    }

    @Test
    public void deveFiltrarERetornarUmaListaEVerificarSeExisteNome() {
        setup();
        Assert.assertNotNull(repository.findFilterList(new TagsRequestDTO(tags), null));
        Assert.assertTrue(repository.existsByNome(tags.getNome()));
    }
}
