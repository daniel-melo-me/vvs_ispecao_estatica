package com.vagas.cadastro.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "curriculo")
@ToString
public class Curriculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String descricao;
    @Column(nullable = false)
    private String titulo;

    @OneToOne
    @JoinColumn(name = "arquivo_id", nullable = false)
    private Arquivo arquivo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tags_curriculo",
            joinColumns = @JoinColumn(name = "curriculo_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private List<Tags> tags;

}
