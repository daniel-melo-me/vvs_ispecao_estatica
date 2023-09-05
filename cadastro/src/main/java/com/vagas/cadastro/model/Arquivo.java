package com.vagas.cadastro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "arquivo")
@ToString
public class Arquivo implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    private String fileType;

    @Lob
    @JsonIgnore
    private byte[] data;

    public Arquivo(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.fileType = contentType;
        this.data = bytes;
    }
}
