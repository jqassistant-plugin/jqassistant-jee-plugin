package org.jqassistant.plugin.jee.jpa.test.set.entity.jakarta;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = JakartaSingleNamedQueryEntity.TESTQUERY_NAME, query = JakartaSingleNamedQueryEntity.TESTQUERY_QUERY)
public class JakartaSingleNamedQueryEntity {

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
