package org.jqassistant.plugin.jee.jpa2.scanner;

import java.io.IOException;
import java.io.InputStream;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.shared.xml.JAXBHelper;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.PropertyDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.api.scanner.JavaScope;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;
import com.buschmais.jqassistant.plugin.xml.api.model.XmlFileDescriptor;

import https.jakarta_ee.xml.ns.persistence.Persistence;
import https.jakarta_ee.xml.ns.persistence.PersistenceUnitCachingType;
import https.jakarta_ee.xml.ns.persistence.PersistenceUnitTransactionType;
import https.jakarta_ee.xml.ns.persistence.PersistenceUnitValidationModeType;
import org.jqassistant.plugin.jee.jpa2.model.PersistenceUnitDescriptor;
import org.jqassistant.plugin.jee.jpa2.model.PersistenceXmlDescriptor;

/**
 * A scanner for JPA model units.
 */
@Requires(XmlFileDescriptor.class)
public class PersistenceXmlScannerPlugin extends AbstractScannerPlugin<FileResource, PersistenceXmlDescriptor> {

    private static final String JPA3_NAMEPSACE = "https://jakarta.ee/xml/ns/persistence";

    private JAXBHelper<Persistence> jaxbHelper;

    @Override
    public void initialize() {
        this.jaxbHelper = new JAXBHelper<>(Persistence.class, JPA3_NAMEPSACE);
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        return JavaScope.CLASSPATH.equals(scope) && "/META-INF/persistence.xml".equals(path) || "/WEB-INF/persistence.xml".equals(path);
    }

    @Override
    public PersistenceXmlDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {
        Persistence persistence = unmarshal(item);

        ScannerContext scannerContext = scanner.getContext();
        Store store = scannerContext.getStore();
        XmlFileDescriptor xmlFileDescriptor = scannerContext.getCurrentDescriptor();
        PersistenceXmlDescriptor persistenceXmlDescriptor = store.addDescriptorType(xmlFileDescriptor, PersistenceXmlDescriptor.class);
        persistenceXmlDescriptor.setVersion(persistence.getVersion());

        // Create model units
        for (Persistence.PersistenceUnit persistenceUnit : persistence.getPersistenceUnit()) {
            PersistenceUnitDescriptor persistenceUnitDescriptor = getPersistenceUnitDescriptor(persistenceUnit, store, scannerContext);
            // Add model unit to model descriptor
            persistenceXmlDescriptor.getContains()
                    .add(persistenceUnitDescriptor);
        }
        return persistenceXmlDescriptor;
    }

    private static PersistenceUnitDescriptor getPersistenceUnitDescriptor(Persistence.PersistenceUnit persistenceUnit, Store store,
            ScannerContext scannerContext) {
        PersistenceUnitDescriptor persistenceUnitDescriptor = store.create(PersistenceUnitDescriptor.class);
        persistenceUnitDescriptor.setName(persistenceUnit.getName());
        PersistenceUnitTransactionType transactionType = persistenceUnit.getTransactionType();
        if (transactionType != null) {
            persistenceUnitDescriptor.setTransactionType(transactionType.value());
        }
        persistenceUnitDescriptor.setDescription(persistenceUnit.getDescription());
        persistenceUnitDescriptor.setJtaDataSource(persistenceUnit.getJtaDataSource());
        persistenceUnitDescriptor.setNonJtaDataSource(persistenceUnit.getNonJtaDataSource());
        persistenceUnitDescriptor.setProvider(persistenceUnit.getProvider());
        persistenceUnitDescriptor.setExcludingUnlistedClasses(persistenceUnit.isExcludeUnlistedClasses());
        PersistenceUnitValidationModeType validationMode = persistenceUnit.getValidationMode();

        if (validationMode != null) {
            persistenceUnitDescriptor.setValidationMode(validationMode.value());
        }

        PersistenceUnitCachingType sharedCacheMode = persistenceUnit.getSharedCacheMode();
        if (sharedCacheMode != null) {
            persistenceUnitDescriptor.setSharedCacheMode(sharedCacheMode.value());
        }

        for (String clazz : persistenceUnit.getClazz()) {
            TypeDescriptor typeDescriptor = scannerContext.peek(TypeResolver.class)
                    .resolve(clazz, scannerContext)
                    .getTypeDescriptor();
            persistenceUnitDescriptor.getContains()
                    .add(typeDescriptor);
        }
        // Create persistence unit properties
        Persistence.PersistenceUnit.Properties properties = persistenceUnit.getProperties();
        if (properties != null) {
            for (Persistence.PersistenceUnit.Properties.Property property : properties.getProperty()) {
                PropertyDescriptor propertyDescriptor = store.create(PropertyDescriptor.class);
                propertyDescriptor.setName(property.getName());
                propertyDescriptor.setValue(property.getValue());
                persistenceUnitDescriptor.getProperties()
                        .add(propertyDescriptor);
            }
        }
        return persistenceUnitDescriptor;
    }

    private Persistence unmarshal(FileResource fileResource) throws IOException {
        try (InputStream inputStream = fileResource.createStream()) {
            return jaxbHelper.unmarshal(inputStream);
        }
    }
}
