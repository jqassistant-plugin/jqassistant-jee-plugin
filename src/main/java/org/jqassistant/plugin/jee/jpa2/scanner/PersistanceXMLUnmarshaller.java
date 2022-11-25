package org.jqassistant.plugin.jee.jpa2.scanner;

import java.io.IOException;

import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.xml.api.scanner.FileResourceJAXBUnmarshaller;
import com.buschmais.jqassistant.plugin.xml.api.scanner.XMLFileFilter;

import com.sun.java.xml.ns.persistence.Persistence;

public class PersistanceXMLUnmarshaller {
    private static String JPA_20_NAMESPACE = "http://java.sun.com/xml/ns/persistence";
    private static String JPA_21_NAMEPSACE = "http://xmlns.jcp.org/xml/ns/persistence";

    public PersistenceView unmarshal(FileResource fileResource) throws IOException {
        PersistenceView result = null;
        if (XMLFileFilter.rootElementMatches(fileResource, null, "persistence", JPA_20_NAMESPACE)) {
            FileResourceJAXBUnmarshaller<Persistence> unmarshaller = new FileResourceJAXBUnmarshaller<>(Persistence.class);
            Persistence persistence = unmarshaller.unmarshal(fileResource);
            result = new PersistenceView(persistence);
        } else if (XMLFileFilter.rootElementMatches(fileResource, null, "persistence", JPA_21_NAMEPSACE)) {
            FileResourceJAXBUnmarshaller<org.jcp.xmlns.xml.ns.persistence.Persistence> unmarshaller = new FileResourceJAXBUnmarshaller<>(
                    org.jcp.xmlns.xml.ns.persistence.Persistence.class);
            org.jcp.xmlns.xml.ns.persistence.Persistence persistence = unmarshaller.unmarshal(fileResource);
            result = new PersistenceView(persistence);
        }

        return result;
    }
}
