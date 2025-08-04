package br.com.estudos.doc_processor.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "doc_tag")
@IdClass(DocTag.DocTagId.class)
public class DocTag extends PanacheEntityBase {

    @Id
    @Column(name = "doc_id")
    public Long docId;

    @Id
    @Column(name = "tag_id")
    public Long tagId;

    @Data
    @NoArgsConstructor
    public static class DocTagId implements Serializable {
        private Long docId;
        private Long tagId;
    }
}