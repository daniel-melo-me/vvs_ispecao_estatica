package com.vagas.cadastro.service.impl;

import com.vagas.cadastro.config.request.EmailRequestDTO;
import com.vagas.cadastro.model.Email;
import com.vagas.cadastro.repository.EmailRepository;
import com.vagas.cadastro.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository repository;
    private final JavaMailSender emailSender;

    @Override
    public Email sendEmail(EmailRequestDTO dto) {
        if (validateField(dto)) {
            throw new RuntimeException("Campo de e-mail nulo");
        }
        return saveEmail(dto);
    }

    private boolean validateField(EmailRequestDTO dto) {

        return isNull(dto.getEmailTo()) &&
                isNull(dto.getSubject()) &&
                isNull(dto.getText());
    }

    public Email saveEmail(EmailRequestDTO dto) {
        dto.setSendDateEmail(LocalDateTime.now());

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(dto.getEmailFrom());
            simpleMailMessage.setTo(dto.getEmailTo());
            simpleMailMessage.setSubject(dto.getSubject());
            simpleMailMessage.setText(dto.getText());
            emailSender.send(simpleMailMessage);

            Email email = new Email();
            BeanUtils.copyProperties(dto, email);

            return repository.saveAndFlush(email);

        } catch (MailException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
