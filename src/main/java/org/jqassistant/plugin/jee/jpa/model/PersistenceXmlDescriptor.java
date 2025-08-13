package org.jqassistant.plugin.jee.jpa.model;

import java.util.List;

import com.buschmais.jqassistant.plugin.xml.api.model.XmlFileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * A descriptor for JPA model descriptors.
 */
@Label("Persistence")
public interface PersistenceXmlDescriptor extends XmlFileDescriptor, JpaDescriptor {

    @Property("version")
    String getVersion();

    void setVersion(String version);

    @Property("contains")
    List<PersistenceUnitDescriptor> getContains();

}
