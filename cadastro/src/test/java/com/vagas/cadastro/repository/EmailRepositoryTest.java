package com.vagas.cadastro.repository;

import com.vagas.cadastro.model.Email;
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
public class EmailRepositoryTest {

    @Autowired
    private EmailRepository repository;

    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    public void deveEnviarOEmailEVerificarNoBanco() {
        Email email = new Email();
        email.setEmailTo("xajaho8581@rxcay.com");
        email.setSubject("Recuperação de senha!");
        email.setText("sua nova senha será: sdahufiusdahfudsa");
        repository.save(email);

        Assert.assertTrue(repository.existsById(email.getId()));
    }
}
