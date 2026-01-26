package org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.javax;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = JavaxSingleNamedQueryEntity.TESTQUERY_NAME, query = JavaxSingleNamedQueryEntity.TESTQUERY_QUERY)
public class JavaxSingleNamedQueryEntity {

    public static final String TESTQUERY_NAME = "namedQuery";
    public static final String TESTQUERY_QUERY = "SELECT e FROM SingleNamedQueryEntity e";

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
