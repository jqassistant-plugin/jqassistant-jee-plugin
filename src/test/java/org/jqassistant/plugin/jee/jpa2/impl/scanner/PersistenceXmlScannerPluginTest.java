package org.jqassistant.plugin.jee.jpa2.impl.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.core.test.mockito.MethodNotMockedAnswer;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.PropertyDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.api.scanner.JavaScope;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeCache;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;
import org.jqassistant.plugin.jee.jpa2.model.PersistenceUnitDescriptor;
import org.jqassistant.plugin.jee.jpa2.model.PersistenceXmlDescriptor;

import org.jqassistant.plugin.jee.jpa2.scanner.PersistenceXmlScannerPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PersistenceXmlScannerPluginTest {

    private static org.jqassistant.plugin.jee.jpa2.scanner.PersistenceXmlScannerPlugin plugin;

    @Mock
    TypeResolver typeResolver;

    @Mock
    FileResource item4V20;

    @Mock
    FileResource itemMinimal4V20;

    @Mock
    FileResource itemMinimal4V21;

    @Mock
    FileResource item4V21;

    @Mock
    Store store;

    @Mock
    ScannerContext context;

    @Mock
    FileDescriptor fileDescriptor;

    @Mock
    TypeDescriptor jpaEntityDescriptor;

    @Mock
    TypeCache.CachedType<TypeDescriptor> cachedType;

    @Mock
    Scanner scanner;

    @Mock
    PersistenceXmlDescriptor persistenceDescriptor;

    @Spy
    List<PersistenceUnitDescriptor> persistenceUnitList = new LinkedList<>();

    @Spy
    List<TypeDescriptor> persistenceEntities = new LinkedList<>();

    @Mock
    PersistenceUnitDescriptor unitDescriptor;

    @Mock
    PropertyDescriptor propertyDescriptor;

    @Spy
    Set<PropertyDescriptor> properties = new HashSet<>();

    private String path = "/META-INF/persistence.xml";

    @BeforeEach
    public void createScanner() {
        plugin = new PersistenceXmlScannerPlugin();
        plugin.initialize();
    }

    @BeforeEach
    void configureMocks() throws IOException {
        doReturn(persistenceEntities).when(unitDescriptor).getContains();
        doReturn(store).when(context).getStore();

        doAnswer(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock invocationOnMock) throws Throwable {
                return PersistenceXmlScannerPluginTest.class.getResourceAsStream("/jpa2/2_0/full/META-INF/persistence.xml");
            }
        }).when(item4V20).createStream();

        doAnswer(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock invocationOnMock) throws Throwable {
                return PersistenceXmlScannerPluginTest.class.getResourceAsStream("/jpa2/2_1/full/META-INF/persistence.xml");
            }
        }).when(item4V21).createStream();


        doAnswer(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock invocationOnMock) throws Throwable {
                return PersistenceXmlScannerPluginTest.class.getResourceAsStream("/jpa2/2_0/minimal/META-INF/persistence.xml");
            }
        }).when(itemMinimal4V20).createStream();

        doAnswer(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock invocationOnMock) throws Throwable {
                return PersistenceXmlScannerPluginTest.class.getResourceAsStream("/jpa2/2_1/minimal/META-INF/persistence.xml");
            }
        }).when(itemMinimal4V21).createStream();


        doReturn(properties).when(unitDescriptor).getProperties();
        doReturn(propertyDescriptor).when(store).create(PropertyDescriptor.class);
        doReturn(jpaEntityDescriptor).when(cachedType).getTypeDescriptor();
        doReturn(cachedType).when(typeResolver).resolve(eq("org.jqassistant.plugin.jee.jpa2.test.set.entity.JpaEntity"),
                eq(context));
        doReturn(typeResolver).when(context).peek(TypeResolver.class);
        doReturn(context).when(scanner).getContext();
        doReturn(fileDescriptor).when(context).getCurrentDescriptor();
        doReturn(persistenceDescriptor).when(store).addDescriptorType(fileDescriptor, PersistenceXmlDescriptor.class);
        doReturn(persistenceUnitList).when(persistenceDescriptor).getContains();
        doReturn(true).when(persistenceDescriptor).isXmlWellFormed();
        doReturn(unitDescriptor).when(store).create(PersistenceUnitDescriptor.class);
    }

    @Test
    void scannerAcceptsIfInClasspathScope() throws IOException {
        FileResource item = Mockito.mock(FileResource.class, new MethodNotMockedAnswer());
        String path = "/META-INF/persistence.xml";
        Scope scope = JavaScope.CLASSPATH;

        assertThat(plugin.accepts(item, path, scope), is(true));
    }

    @Test
    void scannerAcceptsIfPersistenceXMLIsInMETAINF() throws Exception {
        FileResource item = Mockito.mock(FileResource.class, new MethodNotMockedAnswer());
        String path = "/META-INF/persistence.xml";
        Scope scope = JavaScope.CLASSPATH;

        assertThat(plugin.accepts(item, path, scope), is(true));
    }


    @Test
    void scannerAcceptsIfPersistenceXMLIsInWEBINF() throws Exception {
        FileResource item = Mockito.mock(FileResource.class, new MethodNotMockedAnswer());
        String path = "/WEB-INF/persistence.xml";
        Scope scope = JavaScope.CLASSPATH;

        assertThat(plugin.accepts(item, path, scope), is(true));
    }

    @Test
    void scannerFindAllPropertisInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        Mockito.verify(store, times(1)).create(PropertyDescriptor.class);
        Mockito.verify(propertyDescriptor, times(1)).setValue("stringValue");
        Mockito.verify(propertyDescriptor, times(1)).setName("stringProperty");
        Mockito.verify(properties).add(eq(propertyDescriptor));
    }

    @Test
    void scannerFindAllPropertisInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        Mockito.verify(store, times(1)).create(PropertyDescriptor.class);
        Mockito.verify(propertyDescriptor, times(1)).setValue("stringValue");
        Mockito.verify(propertyDescriptor, times(1)).setName("stringProperty");
        Mockito.verify(properties).add(eq(propertyDescriptor));
    }

    @Test
    void scannerFindVersionInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        Mockito.verify(persistenceDescriptor).setVersion(eq("2.0"));
    }

    @Test
    void scannerFindVersionInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        Mockito.verify(persistenceDescriptor).setVersion(eq("2.1"));
    }

    @Test
    void scannerFindsOnePersistenceUnitInPersistenceXMLV20() throws IOException {

        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        verify(persistenceUnitList).add(Mockito.any(PersistenceUnitDescriptor.class));
    }

    @Test
    void scannerFindsOnePersistenceUnitInPersistenceXMLV21() throws IOException {

        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        verify(persistenceUnitList).add(Mockito.any(PersistenceUnitDescriptor.class));
    }

    @Test
    void scannerSetsCorrectNameForPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be on persistence unit.", persistenceUnitList, hasSize(1));

        verify(persistenceUnitList.get(0)).setName(eq("persistence-unit"));
    }

    @Test
    void scannerSetsCorrectNameForPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be on persistence unit.", persistenceUnitList, hasSize(1));

        verify(persistenceUnitList.get(0)).setName(eq("persistence-unit"));
    }

    @Test
    void scannerSetcCorrectTransactionTypeForPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat(persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setTransactionType(eq("RESOURCE_LOCAL"));
    }

    @Test
    void scannerSetcCorrectTransactionTypeForPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat(persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setTransactionType(eq("RESOURCE_LOCAL"));
    }

    @Test
    void scannerSetsCorrectDescriptionFoundInPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setDescription(eq("description"));
    }

    @Test
    void scannerSetsCorrectDescriptionFoundInPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setDescription(eq("description"));
    }

    @Test
    void scannerSetsCorrectJTADataSourceFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setJtaDataSource(eq("jtaDataSource"));
    }

    @Test
    void scannerSetsCorrectJTADataSourceFromPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setJtaDataSource(eq("jtaDataSource"));
    }

    @Test
    void scannerSetsCorrectNonJTADataSourceFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setNonJtaDataSource(eq("nonJtaDataSource"));
    }

    @Test
    void scannerSetsCorrectNonJTADataSourceFromPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setNonJtaDataSource(eq("nonJtaDataSource"));
    }

    @Test
    void scannerSetsCorrectProviderFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setProvider(eq("provider"));
    }

    @Test
    void scannerSetsCorrectProviderFromPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setProvider(eq("provider"));
    }

    @Test
    void scannerSetsCorrectValidationModeFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setValidationMode(eq("AUTO"));
    }

    @Test
    void scannerSetsCorrectValidationModeFromPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setValidationMode(eq("AUTO"));
    }

    @Test
    void scannerSetsCorrectSharedCacheModeFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat(persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setValidationMode(eq("AUTO"));
    }

    @Test
    void scannerSetsCorrectSharedCacheModeFromPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat(persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setValidationMode(eq("AUTO"));
    }


    @Test
    void scannerAddsAllClasseseFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be unit persistence unit.", persistenceUnitList, hasSize(1));
        assertThat("There must be one JPA entity class.", persistenceUnitList.get(0).getContains(), hasSize(1));
        assertThat(persistenceUnitList.get(0).getContains(), hasItem(equalTo(cachedType.getTypeDescriptor())));
    }

    @Test
    void scannerAddsAllClasseseFromPersistenceUnitInPersistenceXMLV21() throws IOException {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be unit persistence unit.", persistenceUnitList, hasSize(1));
        assertThat("There must be one JPA entity class.", persistenceUnitList.get(0).getContains(), hasSize(1));
        assertThat(persistenceUnitList.get(0).getContains(), hasItem(equalTo(cachedType.getTypeDescriptor())));
    }

    @Test
    void scannerSetsExcludeUnlistedClassesToTrueIfNotSpecifiedXMLV20() throws Exception {
        plugin.scan(itemMinimal4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be unit persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setExcludingUnlistedClasses(eq(true));
    }

    @Test
    void scannerSetsExcludeUnlistedClassesToTrueIfNotSpecifiedXMLV21() throws Exception {
        plugin.scan(itemMinimal4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be unit persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setExcludingUnlistedClasses(eq(true));
    }

    @Test
    void scannerSetsExcludeUnlistedClassesAsSpecifiedXMLV20() throws Exception {
        plugin.scan(item4V20, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be unit persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setExcludingUnlistedClasses(eq(false));
    }

    @Test
    void scannerSetsExcludeUnlistedClassesAsSpecifiedXMLV21() throws Exception {
        plugin.scan(item4V21, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be unit persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setExcludingUnlistedClasses(eq(false));
    }
}
