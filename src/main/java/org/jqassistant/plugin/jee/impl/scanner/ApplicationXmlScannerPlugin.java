package org.jqassistant.plugin.jee.impl.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.String;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.shared.xml.JAXBHelper;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.xml.api.model.XmlFileDescriptor;

import https.jakarta_ee.xml.ns.jakartaee.*;
import org.jqassistant.plugin.jee.api.model.*;

import static com.buschmais.jqassistant.core.shared.xml.XmlHelper.rootElementMatches;

/**
 * Scanner plugin for the content of application XML descriptors (i.e.
 * `APP-INF/application.xml`)
 */
@Requires(XmlFileDescriptor.class)
public class ApplicationXmlScannerPlugin extends AbstractScannerPlugin<FileResource, ApplicationXmlDescriptor> {

    private static final String TARGET_NAMESPACE = "https://jakarta.ee/xml/ns/jakartaee";

    private JAXBHelper<ApplicationType> unmarshaller;

    @Override
    public void initialize() {
        unmarshaller = new JAXBHelper<>(ApplicationType.class, TARGET_NAMESPACE);
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        return "/META-INF/application.xml".equals(path) && rootElementMatches(item::createStream, qname -> "application".equals(qname.getLocalPart()));
    }

    @Override
    public ApplicationXmlDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {
        ApplicationType applicationType = getApplicationType(item);
        ScannerContext context = scanner.getContext();
        Store store = context.getStore();
        XmlFileDescriptor xmlFileDescriptor = context.getCurrentDescriptor();
        ApplicationXmlDescriptor applicationXmlDescriptor = context.getStore()
                .addDescriptorType(xmlFileDescriptor, ApplicationXmlDescriptor.class);
        https.jakarta_ee.xml.ns.jakartaee.String applicationName = applicationType.getApplicationName();
        if (applicationName != null) {
            applicationXmlDescriptor.setName(applicationName.getValue());
        }
        applicationXmlDescriptor.setVersion(applicationType.getVersion());
        for (DisplayNameType displayNameType : applicationType.getDisplayName()) {
            DisplayNameDescriptor displayName = XmlDescriptorHelper.createDisplayName(displayNameType, store);
            applicationXmlDescriptor.getDisplayNames()
                    .add(displayName);
        }
        for (DescriptionType descriptionType : applicationType.getDescription()) {
            DescriptionDescriptor description = XmlDescriptorHelper.createDescription(descriptionType, store);
            applicationXmlDescriptor.getDescriptions()
                    .add(description);
        }
        for (IconType iconType : applicationType.getIcon()) {
            IconDescriptor icon = XmlDescriptorHelper.createIcon(iconType, store);
            applicationXmlDescriptor.getIcons()
                    .add(icon);
        }
        GenericBooleanType initializeInOrder = applicationType.getInitializeInOrder();
        if (initializeInOrder != null) {
            applicationXmlDescriptor.setInitializeInOrder(initializeInOrder.getValue());
        }
        PathType libraryDirectory = applicationType.getLibraryDirectory();
        if (libraryDirectory != null) {
            applicationXmlDescriptor.setLibraryDirectory(libraryDirectory.getValue());
        }
        for (ModuleType moduleType : applicationType.getModule()) {
            EnterpriseApplicationModuleDescriptor moduleDescriptor = null;
            PathType pathType = null;
            if (moduleType.getEjb() != null) {
                pathType = moduleType.getEjb();
                moduleDescriptor = store.create(EjbModuleDescriptor.class);
            } else if (moduleType.getWeb() != null) {
                WebType webType = moduleType.getWeb();
                pathType = webType.getWebUri();
                WebModuleDescriptor webModuleDescriptor = store.create(WebModuleDescriptor.class);
                webModuleDescriptor.setContextRoot(webType.getContextRoot()
                        .getValue());
                moduleDescriptor = webModuleDescriptor;
            } else if (moduleType.getConnector() != null) {
                pathType = moduleType.getConnector();
                moduleDescriptor = store.create(ConnectorModuleDescriptor.class);
            } else if (moduleType.getJava() != null) {
                pathType = moduleType.getJava();
                moduleDescriptor = store.create(ClientModuleDescriptor.class);
            }
            if (moduleDescriptor != null) {
                moduleDescriptor.setPath(pathType.getValue());
                applicationXmlDescriptor.getModules()
                        .add(moduleDescriptor);
            }
        }
        for (SecurityRoleType securityRoleType : applicationType.getSecurityRole()) {
            SecurityRoleDescriptor securityRole = XmlDescriptorHelper.createSecurityRole(securityRoleType, store);
            applicationXmlDescriptor.getSecurityRoles()
                    .add(securityRole);
        }
        return applicationXmlDescriptor;
    }

    private ApplicationType getApplicationType(FileResource item) throws IOException {
        try (InputStream inputStream = item.createStream()) {
            return unmarshaller.unmarshal(inputStream);
        }
    }
}
