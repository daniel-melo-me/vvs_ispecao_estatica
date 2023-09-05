package com.vagas.cadastro.config.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
public class EmailRequestDTO {

    @Value("${vagas.email.owner}")
    private String ownerRef;
    @Email
    @Value("${vagas.email.emailFrom}")
    private String emailFrom;
    @Email
    private String emailTo;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime sendDateEmail;
}
