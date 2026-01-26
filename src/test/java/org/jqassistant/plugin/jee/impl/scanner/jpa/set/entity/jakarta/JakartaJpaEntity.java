package org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.jakarta;

import jakarta.persistence.*;

/**
 * A JPA entity.
 */
@Entity
@NamedQueries(@NamedQuery(name = JakartaJpaEntity.TESTQUERY_NAME, query = JakartaJpaEntity.TESTQUERY_QUERY))
public class JakartaJpaEntity {

    public static final String TESTQUERY_NAME = "namedQueries";
    public static final String TESTQUERY_QUERY = "SELECT e FROM JpaEntity e";

    @Id
    @EmbeddedId
    private int id;

    private JakartaJpaEmbeddable embeddable;

    @Embedded
    private JakartaJpaEmbedded embedded;

    @EmbeddedId
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JakartaJpaEmbeddable getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(JakartaJpaEmbeddable embeddable) {
        this.embeddable = embeddable;
    }

    @Embedded
    public JakartaJpaEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(JakartaJpaEmbedded embedded) {
        this.embedded = embedded;
    }
}
