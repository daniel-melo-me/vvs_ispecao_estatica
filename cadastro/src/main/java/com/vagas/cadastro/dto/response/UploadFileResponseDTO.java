package com.vagas.cadastro.dto.response;

import lombok.Data;

@Data
public class UploadFileResponseDTO {

    String id;
    String fileName;
    String fileDownloadUri;
    String fileType;
    long size;

    public UploadFileResponseDTO(String id, String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.id = id;
    }
}
