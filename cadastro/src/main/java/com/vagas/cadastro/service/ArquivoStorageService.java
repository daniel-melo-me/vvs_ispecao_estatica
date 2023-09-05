package com.vagas.cadastro.service;

import com.vagas.cadastro.model.Arquivo;
import com.vagas.cadastro.repository.ArquivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArquivoStorageService {

    private final ArquivoRepository repository;

    public Arquivo storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Arquivo dbFile = new Arquivo(fileName, file.getContentType(), file.getBytes());

            return repository.save(dbFile);
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível armazenar o arquivo " + fileName + ". Por favor, tente novamente!", ex);
        }
    }

    public Arquivo getFile(String fileId) {
        return repository.findById(fileId).orElse(null);
    }

    public Arquivo editFile(String fileId, MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Desculpe! O caminho do arquivo não é válido " + fileName);
            }

            Arquivo dbFile = new Arquivo(fileName, file.getContentType(), file.getBytes());
            dbFile.setId(fileId);

            return repository.save(dbFile);
        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível armazenar o arquivo " + fileName + ". Por favor, tente novamente!", ex);
        }
    }

    public void deleteFile(String fileId) {
        verificarId(fileId);
        repository.deleteById(fileId);
    }

    private void verificarId(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
    }
}