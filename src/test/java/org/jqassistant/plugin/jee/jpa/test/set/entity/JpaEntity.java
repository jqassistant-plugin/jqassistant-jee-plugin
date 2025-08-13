package org.jqassistant.plugin.jee.jpa.test.set.entity;

import javax.persistence.*;

/**
 * A JPA entity.
 */
@Entity
@NamedQueries(@NamedQuery(name = JpaEntity.TESTQUERY_NAME, query = JpaEntity.TESTQUERY_QUERY))
public class JpaEntity {

    public static final String TESTQUERY_NAME = "namedQueries";
    public static final String TESTQUERY_QUERY = "SELECT e FROM JpaEntity e";

    @Id
    @EmbeddedId
    private int id;

    private JpaEmbeddable embeddable;

    @Embedded
    private JpaEmbedded embedded;

    @EmbeddedId
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JpaEmbeddable getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(JpaEmbeddable embeddable) {
        this.embeddable = embeddable;
    }

    @Embedded
    public JpaEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(JpaEmbedded embedded) {
        this.embedded = embedded;
    }
}
