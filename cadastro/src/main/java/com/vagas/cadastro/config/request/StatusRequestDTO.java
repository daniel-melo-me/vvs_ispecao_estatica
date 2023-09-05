package com.vagas.cadastro.config.request;

import com.vagas.cadastro.model.enumeration.StatusEnum;
import lombok.Data;

@Data
public class StatusRequestDTO {

    private StatusEnum status;
}
