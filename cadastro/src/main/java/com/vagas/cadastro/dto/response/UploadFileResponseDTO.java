package com.vagas.cadastro.dto.response;

import lombok.Data;

@Data
public class UploadFileResponseDTO {

    private String id;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponseDTO(String id, String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.id = id;
    }
}
