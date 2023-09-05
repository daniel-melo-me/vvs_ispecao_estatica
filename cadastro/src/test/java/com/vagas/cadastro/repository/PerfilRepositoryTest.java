package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Perfil;
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

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PerfilRepositoryTest {

    @Autowired
    private PerfilRepository repository;

    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    public void deveCriarUmPerfilEVerificarId() {
        Perfil perfil = new Perfil();
        perfil.setNome(PerfilEnum.ROLE_ALUNO);
        repository.save(perfil);

        Assert.assertTrue(repository.existsById(perfil.getId()));
    }
}
