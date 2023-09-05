package com.vagas.cadastro.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidaturas")
@ToString
@Entity
public class Candidaturas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @OneToOne
    @JoinColumn(name = "curriculo_id", nullable = false)
    private Curriculo curriculo;

}
