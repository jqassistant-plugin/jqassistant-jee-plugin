package org.jqassistant.plugin.jee.impl.scanner.cdi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBElement;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.shared.xml.JAXBHelper;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.api.scanner.JavaScope;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;
import com.buschmais.jqassistant.plugin.xml.api.model.XmlFileDescriptor;

import https.jakarta_ee.xml.ns.jakartaee.Beans;
import org.jqassistant.plugin.jee.api.model.cdi.BeansXmlDescriptor;

@Requires(XmlFileDescriptor.class)
public class BeansXmlScannerPlugin extends AbstractScannerPlugin<FileResource, BeansXmlDescriptor> {

    private static final String TARGET_NAMESPACE = "https://jakarta.ee/xml/ns/jakartaee";

    private JAXBHelper<Beans> unmarshaller;

    @Override
    public void initialize() {
        unmarshaller = new JAXBHelper<>(Beans.class, TARGET_NAMESPACE);
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        return JavaScope.CLASSPATH.equals(scope) && ("/META-INF/beans.xml".equals(path) || "/WEB-INF/beans.xml".equals(path));
    }

    @Override
    public BeansXmlDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {
        ScannerContext context = scanner.getContext();
        XmlFileDescriptor xmlFileDescriptor = context.getCurrentDescriptor();
        BeansXmlDescriptor beansXmlDescriptor = context.getStore()
                .addDescriptorType(xmlFileDescriptor, BeansXmlDescriptor.class);
        Beans beans = unmarshal(item);
        beansXmlDescriptor.setVersion(beans.getVersion());
        beansXmlDescriptor.setBeanDiscoveryMode(beans.getBeanDiscoveryMode());
        addTypes(beans.getInterceptors()
                .getClazz(), beansXmlDescriptor.getInterceptors(), context);
        addTypes(beans.getDecorators()
                .getClazz(), beansXmlDescriptor.getDecorators(), context);
        List<JAXBElement<String>> clazzOrStereotype = beans.getAlternatives()
                .getClazzOrStereotype();
        for (JAXBElement<String> element : clazzOrStereotype) {
            TypeDescriptor alternative = scanner.getContext()
                    .peek(TypeResolver.class)
                    .resolve(element.getValue(), context)
                    .getTypeDescriptor();
            beansXmlDescriptor.getAlternatives()
                    .add(alternative);
        }
        return beansXmlDescriptor;
    }

    private Beans unmarshal(FileResource item) throws IOException {
        try (InputStream inputStream = item.createStream()) {
            return unmarshaller.unmarshal(inputStream);
        }
    }

    private void addTypes(List<String> typeNames, List<TypeDescriptor> types, ScannerContext scannerContext) {
        for (String typeName : typeNames) {
            TypeDescriptor type = scannerContext.peek(TypeResolver.class)
                    .resolve(typeName, scannerContext)
                    .getTypeDescriptor();
            types.add(type);
        }
    }
}
