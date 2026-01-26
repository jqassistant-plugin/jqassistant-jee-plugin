package org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.javax;

import javax.persistence.*;

/**
 * A JPA entity.
 */
@Entity
@NamedQueries(@NamedQuery(name = JavaxJpaEntity.TESTQUERY_NAME, query = JavaxJpaEntity.TESTQUERY_QUERY))
public class JavaxJpaEntity {

    public static final String TESTQUERY_NAME = "namedQueries";
    public static final String TESTQUERY_QUERY = "SELECT e FROM JpaEntity e";

    @Id
    @EmbeddedId
    private int id;

    private JavaxJpaEmbeddable embeddable;

    @Embedded
    private JavaxJpaEmbedded embedded;

    @EmbeddedId
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JavaxJpaEmbeddable getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(JavaxJpaEmbeddable embeddable) {
        this.embeddable = embeddable;
    }

    @Embedded
    public JavaxJpaEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(JavaxJpaEmbedded embedded) {
        this.embedded = embedded;
    }
}
