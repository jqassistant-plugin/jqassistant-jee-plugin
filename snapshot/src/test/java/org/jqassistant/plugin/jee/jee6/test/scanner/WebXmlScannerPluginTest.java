package org.jqassistant.plugin.jee.jee6.test.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.java.api.model.ClassFileDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeCache;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;

import org.jqassistant.plugin.jee.jee6.api.model.AuthConstraintDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.DescriptionDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.DisplayNameDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.ErrorPageDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.FilterDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.FilterMappingDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.FormLoginConfigDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.HttpMethodDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.IconDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.ListenerDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.LoginConfigDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.ParamValueDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.RoleNameDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.RunAsDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.SecurityConstraintDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.SecurityRoleDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.ServletDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.ServletMappingDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.SessionConfigDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.UrlPatternDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.UserDataConstraintDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.WebApplicationArchiveDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.WebResourceCollectionDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.WebXmlDescriptor;
import org.jqassistant.plugin.jee.jee6.api.scanner.WebApplicationScope;
import org.jqassistant.plugin.jee.jee6.impl.scanner.WebXmlScannerPlugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class WebXmlScannerPluginTest extends AbstractXmlScannerTest {

    @Mock
    private TypeResolver typeResolver;

    @Mock
    private WebApplicationArchiveDescriptor warDescriptor;

    @Mock
    private FileDescriptor fileDescriptor;

    @Mock
    private WebXmlDescriptor webXmlDescriptor;

    @Mock
    private SessionConfigDescriptor sessionConfigDescriptor;

    @Mock
    private ParamValueDescriptor contextParamDescriptor;

    // Error Page
    @Mock
    private ErrorPageDescriptor errorPageDescriptor;

    // Servlet
    @Mock
    private ServletDescriptor servletDescriptor;

    @Mock
    private DescriptionDescriptor servletDescriptionDescriptor;

    @Mock
    private DisplayNameDescriptor servletDisplayNameDescriptor;

    @Mock
    private IconDescriptor servletIconDescriptor;

    @Mock
    private RunAsDescriptor runAsDescriptor;

    @Mock
    private DescriptionDescriptor servletRunAsDescriptionDescriptor;

    @Mock
    private ParamValueDescriptor servletInitParamDescriptor;

    @Mock
    private ClassFileDescriptor servletClassDescriptor;

    @Mock
    private TypeCache.CachedType<TypeDescriptor> cachedServletClassDescriptor;

    // Servlet Mapping

    @Mock
    private ServletMappingDescriptor servletMappingDescriptor;

    @Mock
    private UrlPatternDescriptor servletUrlMappingDescriptor;

    // Filter
    @Mock
    private FilterDescriptor filterDescriptor;

    @Mock
    private DescriptionDescriptor filterDescriptionDescriptor;

    @Mock
    private DisplayNameDescriptor filterDisplayNameDescriptor;

    @Mock
    private IconDescriptor filterIconDescriptor;

    @Mock
    private ParamValueDescriptor filterInitParamDescriptor;

    @Mock
    private TypeCache.CachedType<TypeDescriptor> cachedFilterClassDescriptor;

    @Mock
    private ClassFileDescriptor filterClassDescriptor;

    // Filter Mapping

    @Mock
    private FilterMappingDescriptor filterMappingDescriptor;

    @Mock
    private UrlPatternDescriptor filterUrlMappingDescriptor;

    // Listener

    @Mock
    private TypeCache.CachedType<TypeDescriptor> cachedListenerClassDescriptor;

    @Mock
    private ClassFileDescriptor listenerClassDescriptor;

    @Mock
    private ListenerDescriptor listenerDescriptor;

    // Security Constraint
    @Mock
    private SecurityConstraintDescriptor securityConstraintDescriptor;

    @Mock
    private DisplayNameDescriptor securityConstraintDisplayNameDescriptor;

    @Mock
    private WebResourceCollectionDescriptor webResourceCollectionDescriptor;

    @Mock
    private DescriptionDescriptor webResourceCollectionDescriptionDescriptor;

    @Mock
    private HttpMethodDescriptor httpMethodDescriptor;

    @Mock
    private UserDataConstraintDescriptor userDataConstraintDescriptor;

    @Mock
    private UrlPatternDescriptor webResourceCollectionUrlPatternDescriptor;

    @Mock
    private AuthConstraintDescriptor authConstraintDescriptor;

    @Mock
    private DescriptionDescriptor authConstraintDescriptionDescriptor;

    @Mock
    private RoleNameDescriptor authConstraintRoleNameDescriptor;

    // Security Role

    @Mock
    private SecurityRoleDescriptor securityRoleDescriptor;

    @Mock
    private DescriptionDescriptor securityRoleDescriptionDescriptor;

    @Mock
    private RoleNameDescriptor securityRoleRoleNameDescriptor;

    // Login Config
    @Mock
    private LoginConfigDescriptor loginConfigDescriptor;

    @Mock
    private FormLoginConfigDescriptor formLoginConfigDescriptor;

    @Test
    void webXml() throws IOException {
        when(scannerContext.peek(TypeResolver.class)).thenReturn(typeResolver);

        FileResource fileResource = mock(FileResource.class);
        when(fileResource.createStream()).thenAnswer(new Answer<InputStream>() {
            @Override
            public InputStream answer(InvocationOnMock invocation) throws Throwable {
                return WebXmlScannerPluginTest.class.getResourceAsStream("/jee6/WEB-INF/web.xml");
            }
        });

        when(scannerContext.getCurrentDescriptor()).thenReturn(fileDescriptor);
        when(store.addDescriptorType(fileDescriptor, WebXmlDescriptor.class)).thenReturn(webXmlDescriptor);
        when(scanner.scan(Mockito.any(StreamSource.class), Mockito.eq("/jee6/WEB-INF/web.xml"), Mockito.eq(WebApplicationScope.WAR))).thenReturn(webXmlDescriptor);
        when(webXmlDescriptor.isXmlWellFormed()).thenReturn(true);
        when(webXmlDescriptor.getContextParams()).thenReturn(mock(List.class));
        when(webXmlDescriptor.getErrorPages()).thenReturn(mock(List.class));
        when(webXmlDescriptor.getServlets()).thenReturn(mock(List.class));
        when(webXmlDescriptor.getServletMappings()).thenReturn(mock(List.class));
        when(webXmlDescriptor.getFilterMappings()).thenReturn(mock(List.class));
        when(webXmlDescriptor.getListeners()).thenReturn(mock(List.class));
        when(webXmlDescriptor.getSecurityConstraints()).thenReturn(mock(List.class));
        when(webXmlDescriptor.getSecurityRoles()).thenReturn(mock(List.class));

        when(store.create(SessionConfigDescriptor.class)).thenReturn(sessionConfigDescriptor);
        when(store.create(ErrorPageDescriptor.class)).thenReturn(errorPageDescriptor);

        // Servlet
        when(store.create(ServletDescriptor.class)).thenReturn(servletDescriptor);
        when(servletDescriptor.getDescriptions()).thenReturn(mock(List.class));
        when(servletDescriptor.getDisplayNames()).thenReturn(mock(List.class));
        when(servletDescriptor.getIcons()).thenReturn(mock(List.class));
        when(servletDescriptor.getInitParams()).thenReturn(mock(List.class));
        when(servletDescriptor.getMappings()).thenReturn(mock(List.class));

        when(store.create(RunAsDescriptor.class)).thenReturn(runAsDescriptor);
        when(runAsDescriptor.getDescriptions()).thenReturn(mock(List.class));

        when(store.create(DescriptionDescriptor.class)).thenReturn(servletDescriptionDescriptor, servletRunAsDescriptionDescriptor, filterDescriptionDescriptor,
                authConstraintDescriptionDescriptor, webResourceCollectionDescriptionDescriptor, securityRoleDescriptionDescriptor, null);
        when(store.create(DisplayNameDescriptor.class)).thenReturn(servletDisplayNameDescriptor, filterDisplayNameDescriptor,
                securityConstraintDisplayNameDescriptor, null);
        when(store.create(IconDescriptor.class)).thenReturn(servletIconDescriptor, filterIconDescriptor, null);

        when(store.create(ParamValueDescriptor.class)).thenReturn(contextParamDescriptor, servletInitParamDescriptor, filterInitParamDescriptor, null);
        when(typeResolver.resolve("com.buschmais.jqassistant.TestServlet", scannerContext)).thenReturn(cachedServletClassDescriptor);
        when(cachedServletClassDescriptor.getTypeDescriptor()).thenReturn(servletClassDescriptor);

        // Servlet Mapping
        when(store.create(ServletMappingDescriptor.class)).thenReturn(servletMappingDescriptor);
        when(store.create(UrlPatternDescriptor.class)).thenReturn(servletUrlMappingDescriptor, filterUrlMappingDescriptor,
                webResourceCollectionUrlPatternDescriptor, null);
        when(servletMappingDescriptor.getUrlPatterns()).thenReturn(mock(List.class));

        // Filter
        when(store.create(FilterDescriptor.class)).thenReturn(filterDescriptor);
        when(webXmlDescriptor.getFilters()).thenReturn(mock(List.class));
        when(typeResolver.resolve("com.buschmais.jqassistant.TestFilter", scannerContext)).thenReturn(cachedFilterClassDescriptor);
        when(cachedFilterClassDescriptor.getTypeDescriptor()).thenReturn(filterClassDescriptor);
        when(filterDescriptor.getDescriptions()).thenReturn(mock(List.class));
        when(filterDescriptor.getDisplayNames()).thenReturn(mock(List.class));
        when(filterDescriptor.getIcons()).thenReturn(mock(List.class));
        when(filterDescriptor.getInitParams()).thenReturn(mock(List.class));
        when(filterDescriptor.getMappings()).thenReturn(mock(List.class));

        // Filter Mapping
        when(store.create(FilterMappingDescriptor.class)).thenReturn(filterMappingDescriptor);
        when(filterMappingDescriptor.getUrlPatterns()).thenReturn(mock(List.class));

        // Listener
        when(typeResolver.resolve("com.buschmais.jqassistant.TestListener", scannerContext)).thenReturn(cachedListenerClassDescriptor);
        when(cachedListenerClassDescriptor.getTypeDescriptor()).thenReturn(listenerClassDescriptor);
        when(store.create(ListenerDescriptor.class)).thenReturn(listenerDescriptor);

        // Security Constraint
        when(store.create(SecurityConstraintDescriptor.class)).thenReturn(securityConstraintDescriptor);
        when(securityConstraintDescriptor.getDisplayNames()).thenReturn(mock(List.class));
        when(securityConstraintDescriptor.getWebResourceCollections()).thenReturn(mock(List.class));
        when(store.create(WebResourceCollectionDescriptor.class)).thenReturn(webResourceCollectionDescriptor);
        when(webResourceCollectionDescriptor.getDescriptions()).thenReturn(mock(List.class));
        when(webResourceCollectionDescriptor.getUrlPatterns()).thenReturn(mock(List.class));
        when(webResourceCollectionDescriptor.getHttpMethods()).thenReturn(mock(List.class));
        when(store.create(AuthConstraintDescriptor.class)).thenReturn(authConstraintDescriptor);
        when(authConstraintDescriptor.getDescriptions()).thenReturn(mock(List.class));
        when(authConstraintDescriptor.getRoleNames()).thenReturn(mock(List.class));
        when(store.create(RoleNameDescriptor.class)).thenReturn(authConstraintRoleNameDescriptor, securityRoleRoleNameDescriptor, null);
        when(securityConstraintDescriptor.getAuthConstraints()).thenReturn(mock(List.class));
        when(store.create(UserDataConstraintDescriptor.class)).thenReturn(userDataConstraintDescriptor);
        when(securityConstraintDescriptor.getUserDataConstraints()).thenReturn(mock(List.class));
        when(store.create(HttpMethodDescriptor.class)).thenReturn(httpMethodDescriptor);

        // Security Role
        when(store.create(SecurityRoleDescriptor.class)).thenReturn(securityRoleDescriptor);
        when(securityRoleDescriptor.getDescriptions()).thenReturn(mock(List.class));

        // Login Config
        when(store.create(LoginConfigDescriptor.class)).thenReturn(loginConfigDescriptor);
        when(store.create(FormLoginConfigDescriptor.class)).thenReturn(formLoginConfigDescriptor);

        WebXmlScannerPlugin scannerPlugin = new WebXmlScannerPlugin();
        scannerPlugin.initialize();
        scannerPlugin.configure(scannerContext, Collections.<String, Object> emptyMap());
        scannerPlugin.scan(fileResource, "/jee6/WEB-INF/web.xml", WebApplicationScope.WAR, scanner);

        verify(store).addDescriptorType(fileDescriptor, WebXmlDescriptor.class);
        verify(scanner).scan(Mockito.any(StreamSource.class), Mockito.eq("/jee6/WEB-INF/web.xml"), Mockito.eq(WebApplicationScope.WAR));
        verify(webXmlDescriptor).setVersion("3.0");
        verify(webXmlDescriptor).setSessionConfig(sessionConfigDescriptor);
        verify(webXmlDescriptor.getContextParams()).add(contextParamDescriptor);
        verify(contextParamDescriptor).setName("contextParam");
        verify(contextParamDescriptor).setValue("contextParamValue");
        verify(sessionConfigDescriptor).setSessionTimeout(30);

        verifyErrorPage();
        verifyServlet();
        verifyServletMapping();
        verifyFilter();
        verifyFilterMapping();
        verifyListener();
        verifySecurityConstraint();
        verifySecurityRole(webXmlDescriptor.getSecurityRoles(), securityRoleDescriptor, securityRoleDescriptionDescriptor, securityRoleRoleNameDescriptor, "en",
                "Admin users", "Admin");
        verifyLoginConfig();
    }

    private void verifyLoginConfig() {
        verify(store).create(LoginConfigDescriptor.class);
        verify(loginConfigDescriptor).setAuthMethod("FORM");
        verify(loginConfigDescriptor).setRealmName("TestRealm");
        verify(store).create(FormLoginConfigDescriptor.class);
        verify(loginConfigDescriptor).setFormLoginConfig(formLoginConfigDescriptor);
        verify(formLoginConfigDescriptor).setFormLoginPage("/login.jsp");
        verify(formLoginConfigDescriptor).setFormErrorPage("/error.jsp");
    }

    private void verifySecurityConstraint() {
        verify(store).create(SecurityConstraintDescriptor.class);
        verify(webXmlDescriptor.getSecurityConstraints()).add(securityConstraintDescriptor);
        verifyDisplayName(securityConstraintDescriptor.getDisplayNames(), securityConstraintDisplayNameDescriptor, "en", "Security Constraint");
        verify(securityConstraintDescriptor.getWebResourceCollections()).add(webResourceCollectionDescriptor);
        verify(webResourceCollectionDescriptor).setName("secureResource");
        verifyDescription(webResourceCollectionDescriptor.getDescriptions(), webResourceCollectionDescriptionDescriptor, "en", "Web Resource Description");
        verify(webResourceCollectionDescriptor.getUrlPatterns()).add(webResourceCollectionUrlPatternDescriptor);
        verify(webResourceCollectionUrlPatternDescriptor).setValue("/secureResource/*");
        verify(webResourceCollectionDescriptor.getHttpMethods()).add(httpMethodDescriptor);
        verify(httpMethodDescriptor).setName("get");
        verify(securityConstraintDescriptor.getAuthConstraints()).add(authConstraintDescriptor);
        verifyDescription(authConstraintDescriptor.getDescriptions(), authConstraintDescriptionDescriptor, "en", "Auth Constraint Description");
        verify(authConstraintDescriptor.getRoleNames()).add(authConstraintRoleNameDescriptor);
        verify(authConstraintRoleNameDescriptor).setName("Admin");
        verify(securityConstraintDescriptor.getUserDataConstraints()).add(userDataConstraintDescriptor);
        verify(userDataConstraintDescriptor).setTransportGuarantee("CONFIDENTIAL");
    }

    private void verifyErrorPage() {
        verify(store).create(ErrorPageDescriptor.class);
        verify(webXmlDescriptor.getErrorPages()).add(errorPageDescriptor);
        verify(errorPageDescriptor).setErrorPage("/errorpage.jsp");
    }

    private void verifyListener() {
        verify(store).create(FilterDescriptor.class);
        verify(webXmlDescriptor.getListeners()).add(listenerDescriptor);
        verify(listenerDescriptor).setType(listenerClassDescriptor);
    }

    private void verifyFilterMapping() {
        verify(store).create(FilterMappingDescriptor.class);
        verify(filterDescriptor.getMappings()).add(filterMappingDescriptor);
        verify(filterMappingDescriptor.getUrlPatterns()).add(filterUrlMappingDescriptor);
        verify(filterUrlMappingDescriptor).setValue("/*");
    }

    private void verifyFilter() {
        verify(store).create(FilterDescriptor.class);
        verify(webXmlDescriptor.getFilters()).add(filterDescriptor);
        verify(filterDescriptor).setName("TestFilter");
        verify(filterDescriptor).setType(filterClassDescriptor);
        verifyDescription(filterDescriptor.getDescriptions(), filterDescriptionDescriptor, "en", "Test Filter Description");
        verifyDisplayName(filterDescriptor.getDisplayNames(), filterDisplayNameDescriptor, "en", "Test Filter");
        verifyIcon(filterDescriptor.getIcons(), filterIconDescriptor, "filterIcon-small.png", "filterIcon-large.png");
        verifyInitParam(filterDescriptor.getInitParams(), filterInitParamDescriptor, "filterParam", "filterParamValue");
        verify(filterDescriptor.getMappings()).add(filterMappingDescriptor);
    }

    private void verifyServletMapping() {
        verify(store).create(ServletMappingDescriptor.class);
        verify(webXmlDescriptor.getServletMappings()).add(servletMappingDescriptor);
        verify(store, times(3)).create(UrlPatternDescriptor.class);
        verify(servletMappingDescriptor.getUrlPatterns()).add(servletUrlMappingDescriptor);
        verify(servletUrlMappingDescriptor).setValue("/*");
    }

    private void verifyServlet() {
        verify(webXmlDescriptor.getServlets()).add(servletDescriptor);
        verify(servletDescriptor).setName("TestServlet");
        verify(servletDescriptor).setType(servletClassDescriptor);
        verifyDescription(servletDescriptor.getDescriptions(), servletDescriptionDescriptor, "en", "Test Servlet Description");
        verifyDisplayName(servletDescriptor.getDisplayNames(), servletDisplayNameDescriptor, "en", "Test Servlet");
        verifyIcon(servletDescriptor.getIcons(), servletIconDescriptor, "servletIcon-small.png", "servletIcon-large.png");
        verifyInitParam(servletDescriptor.getInitParams(), servletInitParamDescriptor, "servletParam", "servletParamValue");
        verify(webXmlDescriptor.getServletMappings()).add(servletMappingDescriptor);
        verify(servletDescriptor).setLoadOnStartup("1");
        verify(servletDescriptor).setEnabled(true);
        verify(servletDescriptor).setAsyncSupported(true);
        verify(servletDescriptor).setRunAs(runAsDescriptor);
        verify(runAsDescriptor).setRoleName("Admin");
        verify(runAsDescriptor.getDescriptions()).add(servletRunAsDescriptionDescriptor);
        verify(servletRunAsDescriptionDescriptor).setLang("en");
        verify(servletRunAsDescriptionDescriptor).setValue("Administrator");
    }

    private void verifyInitParam(List<ParamValueDescriptor> descriptors, ParamValueDescriptor descriptor, String name, String value) {
        verify(descriptors).add(descriptor);
        verify(descriptor).setName(name);
        verify(descriptor).setValue(value);
    }

}
