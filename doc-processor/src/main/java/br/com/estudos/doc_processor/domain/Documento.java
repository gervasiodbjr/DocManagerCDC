package br.com.estudos.doc_processor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "documento")
public class Documento extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256)
    public String titulo;

    @Column(columnDefinition = "text")
    public String corpo;

    @Column(name = "criado_em")
    public OffsetDateTime criadoEm;

    @Column(name = "atualizado_em")
    public OffsetDateTime atualizadoEm;

    @Column(name = "departamento_id")
    public Integer departamentoId;
}
