package org.jqassistant.plugin.jee.impl.scanner.jpa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.common.api.model.PropertyDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.hamcrest.Matcher;
import org.jqassistant.plugin.jee.api.model.jpa.PersistenceUnitDescriptor;
import org.jqassistant.plugin.jee.api.model.jpa.PersistenceXmlDescriptor;
import org.jqassistant.plugin.jee.impl.scanner.jpa.matcher.PersistenceUnitMatcher;
import org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.jakarta.JakartaJpaEmbeddable;
import org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.jakarta.JakartaJpaEntity;
import org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.jakarta.JakartaSingleNamedQueryEntity;
import org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.javax.JavaxJpaEmbeddable;
import org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.javax.JavaxJpaEntity;
import org.jqassistant.plugin.jee.impl.scanner.jpa.set.entity.javax.JavaxSingleNamedQueryEntity;
import org.jqassistant.plugin.jee.jpa.test.set.persistencecontext.jakarta.JakartaTypeWithPersistenceContext;
import org.jqassistant.plugin.jee.jpa.test.set.persistencecontext.javax.JavaxTypeWithPersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.plugin.java.test.matcher.FieldDescriptorMatcher.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.ValueDescriptorMatcher.valueDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Tests for the JPA concepts.
 */
class JpaIT extends AbstractJavaPluginIT {

    public static final String SCHEMA_2_0 = "2.0";
    public static final String SCHEMA_2_1 = "2.1";
    public static final String SCHEMA_3_2 = "3.2";

    /**
     * Verifies the concept `jpa:Entity`.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void entity(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("jpa:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Type:JPA:Entity) RETURN e").getColumn("e"), hasItem(typeDescriptor(classToScan)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "jpa:Embeddable".
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEmbeddable.class, JakartaJpaEmbeddable.class})
    void embeddable(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("jpa:Embeddable").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Type:JPA:Embeddable) RETURN e").getColumn("e"), hasItem(typeDescriptor(classToScan)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "jpa:Embedded".
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void embedded(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("jpa:Embedded").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> members = query("MATCH (e:JPA:Embedded) RETURN e").getColumn("e");
        assertThat(members, hasItem(fieldDescriptor(classToScan, "embedded")));
        assertThat(members, hasItem(methodDescriptor(classToScan, "getEmbedded")));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "jpa:EmbeddedId".
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void embeddedId(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("jpa:EmbeddedId").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> members = query("MATCH (e:JPA:EmbeddedId) RETURN e").getColumn("e");
        assertThat(members, hasItem(fieldDescriptor(classToScan, "id")));
        assertThat(members, hasItem(methodDescriptor(classToScan, "getId")));
        store.commitTransaction();
    }

    @ParameterizedTest
    @MethodSource("classesWithPersistenceContextAndEntityManager")
    void persistenceContext(Class<?> typeWithPersistenceContext, Class<?> entityManager) throws Exception {
        scanClasses(typeWithPersistenceContext);
        assertThat(applyConcept("jpa:PersistenceContextField").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> members = query("MATCH (e:JPA:PersistenceContext:InjectionPoint:JEE) RETURN e").getColumn("e");
        assertThat(members, hasItem(fieldDescriptor(typeWithPersistenceContext, "entityManager")));
        List<Object> injectables = query("MATCH (e:Type:Injectable) RETURN e").getColumn("e");
        assertThat(injectables, hasItem(typeDescriptor(entityManager)));
        store.commitTransaction();
    }

    private static Stream<Arguments> classesWithPersistenceContextAndEntityManager() {
        return Stream.of(
                Arguments.of(JavaxTypeWithPersistenceContext.class, javax.persistence.EntityManager.class),
                Arguments.of(JakartaTypeWithPersistenceContext.class, jakarta.persistence.EntityManager.class));
    }

    private static Stream<Arguments> entityClasses() {
        return Stream.of(Arguments.of(JavaxJpaEntity.class, JavaxSingleNamedQueryEntity.class, JavaxJpaEntity.TESTQUERY_NAME,
                        JavaxJpaEntity.TESTQUERY_QUERY, JavaxSingleNamedQueryEntity.TESTQUERY_NAME, JavaxSingleNamedQueryEntity.TESTQUERY_QUERY),
                Arguments.of(JakartaJpaEntity.class, JakartaSingleNamedQueryEntity.class, JakartaJpaEntity.TESTQUERY_NAME,
                        JakartaJpaEntity.TESTQUERY_QUERY, JakartaSingleNamedQueryEntity.TESTQUERY_NAME, JakartaSingleNamedQueryEntity.TESTQUERY_QUERY));

    }

    /**
     * Verifies the concept "jpa:NamedQuery".
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @MethodSource("entityClasses")
    void namedQueries(Class<?> jpaEntity, Class<?> namedQueryEntity, String jpaTestQueryName, String jpaTestQueryQuery, String singleNameQueryTestQueryName, String singleNameQueryTestQueryQuery) throws Exception {
        scanClasses(jpaEntity, namedQueryEntity);
        assertThat(applyConcept("jpa:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyNamedQuery(jpaEntity, jpaTestQueryName, jpaTestQueryQuery);
        verifyNamedQuery(namedQueryEntity, singleNameQueryTestQueryName, singleNameQueryTestQueryQuery);
        store.commitTransaction();
    }

    /**
     * Verify the result
     *
     * @param entity
     *         The entity class to verify.
     * @param name
     *         The name of the defined query.
     * @param query
     *         The query.
     */
    private void verifyNamedQuery(Class<?> entity, String name, String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("entity", entity.getName());
        TestResult result = query("MATCH (e:Entity {fqn:$entity})-[:DEFINES]->(n:JPA:NamedQuery) RETURN n.name as name, n.query as query", params);
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        assertThat((String) row.get("name"), equalTo(name));
        assertThat((String) row.get("query"), equalTo(query));
    }

    /**
     * Verifies the uniqueness of concept "jpa:NamedQuery" with keeping existing properties.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @MethodSource("entityClasses")
    void namedQueryUniqueDifferentQuery(Class<?> jpaEntity, Class<?> namedQueryEntity, String jpaTestQueryName, String jpaTestQueryQuery, String singleNameQueryTestQueryName, String singleNameQueryTestQueryQuery) throws Exception {
        scanClasses(jpaEntity, namedQueryEntity);
        assertThat(applyConcept("jpa:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(
                query("MATCH (e:JPA:Entity {name: '"+jpaEntity.getSimpleName()+"'}) CREATE (e)-[:DEFINES]->(n:JPA:NamedQuery {name: 'namedQueries', prop: 'value', query: 'foo'}) RETURN n").getColumn(
                                "n")
                        .size(), equalTo(1));
        assertThat(
                query("MATCH (e:JPA:Entity {name: '"+namedQueryEntity.getSimpleName()+"'}) CREATE (e)-[:DEFINES]->(n:JPA:NamedQuery {name: 'namedQuery', prop: 'value', query: 'foo'}) RETURN n").getColumn(
                                "n")
                        .size(), equalTo(1));
        assertThat(query("CREATE (n:JPA:NamedQuery {name: 'otherQuery', query: 'SELECT e'}) RETURN n").getColumn("n")
                .size(), equalTo(1));
        verifyUniqueRelation(jpaTestQueryName, jpaTestQueryQuery, 0, 0);
        verifyUniqueRelation(singleNameQueryTestQueryName, singleNameQueryTestQueryQuery, 0, 0);
        store.commitTransaction();
        assertThat(applyConcept("jpa:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyUniqueRelation(jpaTestQueryName, jpaTestQueryQuery, 1, 0);
        verifyUniqueRelation(singleNameQueryTestQueryName, singleNameQueryTestQueryQuery, 1, 0);
        store.commitTransaction();
    }

    /**
     * Verifies the uniqueness of concept "jpa:NamedQuery" with keeping existing properties.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @MethodSource("entityClasses")
    void namedQueryUniqueSameQuery(Class<?> jpaEntity, Class<?> namedQueryEntity, String jpaTestQueryName, String jpaTestQueryQuery, String singleNameQueryTestQueryName, String singleNameQueryTestQueryQuery) throws Exception {
        scanClasses(jpaEntity, namedQueryEntity);
        assertThat(applyConcept("jpa:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(
                query("MATCH (e:JPA:Entity {name: '"+jpaEntity.getSimpleName()+"'}) CREATE (e)-[:DEFINES]->(n:JPA:NamedQuery {name: 'namedQueries', prop: 'value', query: 'SELECT e FROM JpaEntity e'}) RETURN n").getColumn(
                                "n")
                        .size(), equalTo(1));
        assertThat(
                query("MATCH (e:JPA:Entity {name: '"+namedQueryEntity.getSimpleName()+"'}) CREATE (e)-[:DEFINES]->(n:JPA:NamedQuery {name: 'namedQuery', prop: 'value', query: 'SELECT e FROM SingleNamedQueryEntity e'}) RETURN n").getColumn(
                                "n")
                        .size(), equalTo(1));
        assertThat(query("CREATE (n:JPA:NamedQuery {name: 'otherQuery', query: 'SELECT e'}) RETURN n").getColumn("n")
                .size(), equalTo(1));
        verifyUniqueRelation(jpaTestQueryName, jpaTestQueryQuery, 1, 0);
        verifyUniqueRelation(singleNameQueryTestQueryName, singleNameQueryTestQueryQuery, 1, 0);
        store.commitTransaction();
        assertThat(applyConcept("jpa:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyUniqueRelation(jpaTestQueryName, jpaTestQueryQuery, 1, 0);
        verifyUniqueRelation(singleNameQueryTestQueryName, singleNameQueryTestQueryQuery, 1, 0);
        store.commitTransaction();
    }

    /**
     * Verifies the uniqueness of concept "jpa:NamedQuery" with keeping existing properties.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @MethodSource("entityClasses")
    void namedQueryUniqueWithoutQuery(Class<?> jpaEntity, Class<?> namedQueryEntity, String jpaTestQueryName, String jpaTestQueryQuery, String singleNameQueryTestQueryName, String singleNameQueryTestQueryQuery) throws Exception {
        scanClasses(jpaEntity, namedQueryEntity);
        assertThat(applyConcept("jpa:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(
                query("MATCH (e:JPA:Entity {name: '"+jpaEntity.getSimpleName()+"'}) CREATE (e)-[:DEFINES]->(n:JPA:NamedQuery {name: 'namedQueries', prop: 'value'}) RETURN n").getColumn(
                                "n")
                        .size(), equalTo(1));
        assertThat(
                query("MATCH (e:JPA:Entity {name: '"+namedQueryEntity.getSimpleName()+"'}) CREATE (e)-[:DEFINES]->(n:JPA:NamedQuery {name: 'namedQuery', prop: 'value'}) RETURN n").getColumn(
                                "n")
                        .size(), equalTo(1));
        assertThat(query("CREATE (n:JPA:NamedQuery {name: 'otherQuery', query: 'SELECT e'}) RETURN n").getColumn("n")
                .size(), equalTo(1));
        verifyUniqueRelation(jpaTestQueryName, jpaTestQueryQuery, 0, 1);
        verifyUniqueRelation(singleNameQueryTestQueryName, singleNameQueryTestQueryQuery, 0, 1);
        store.commitTransaction();
        assertThat(applyConcept("jpa:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyUniqueRelation(jpaTestQueryName, jpaTestQueryQuery, 1, 0);
        verifyUniqueRelation(singleNameQueryTestQueryName, singleNameQueryTestQueryQuery, 1, 0);
        store.commitTransaction();
    }

    /**
     * Verifies scanning of persistence descriptors.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void fullPersistenceDescriptorV20(Class<?> entityClass) {
        persistenceDescriptor("jpa/2_0/full", SCHEMA_2_0, entityClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void fullPersistenceDescriptorV21(Class<?> entityClass) {
        persistenceDescriptor("jpa/2_1/full", SCHEMA_2_1, entityClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void fullPersistenceDescriptorV32(Class<?> entityClass) {
        persistenceDescriptor("jpa/3_2/full", SCHEMA_3_2, entityClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void minimalPersistenceDescriptorV20(Class<?> entityClass) {
        persistenceDescriptor("jpa/2_0/minimal", SCHEMA_2_0, entityClass);
    }

    /**
     * Verifies scanning of persistence descriptors.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void minimalPersistenceDescriptorV21(Class<?> entityClass) {
        persistenceDescriptor("jpa/2_1/minimal", SCHEMA_2_1, entityClass);
    }

    private void persistenceDescriptor(String path, String schema, Class<?> entityClass) {
        scanClassPathDirectory(new File(getClassesDirectory(entityClass), path));
        store.beginTransaction();
        TestResult testResult = query("MATCH (p:JPA:Persistence:Xml) RETURN p");
        assertThat(testResult.getRows()
                .size(), equalTo(1));
        List<? super PersistenceXmlDescriptor> persistenceDescriptors = testResult.getColumn("p");
        PersistenceXmlDescriptor persistenceXmlDescriptor = (PersistenceXmlDescriptor) persistenceDescriptors.get(0);
        assertThat(persistenceXmlDescriptor.getVersion(), equalTo(schema));
        List<PersistenceUnitDescriptor> persistenceUnits = persistenceXmlDescriptor.getContains();
        assertThat(persistenceUnits, hasItem(PersistenceUnitMatcher.persistenceUnitDescriptor("persistence-unit")));
        store.commitTransaction();
    }

    @Test
    void fullPersistenceUnitDescriptorV20() {
        fullPersistenceUnitDescriptor("jpa/2_0/full", JavaxJpaEntity.class);
    }

    /**
     * Verifies scanning of persistence unit descriptors.
     */
    @Test
    void fullPersistenceUnitDescriptorV21() {
        fullPersistenceUnitDescriptor("jpa/2_1/full", JavaxJpaEntity.class);
    }

    /**
     * Verifies scanning of persistence unit descriptors.
     */
    @Test
    void fullPersistenceUnitDescriptorV32() {
        fullPersistenceUnitDescriptor("jpa/3_2/full", JakartaJpaEntity.class);
    }

    private void fullPersistenceUnitDescriptor(String path, Class<?> entityClass) {
        scanClassPathDirectory(new File(getClassesDirectory(entityClass), path));
        store.beginTransaction();
        TestResult testResult = query("MATCH (pu:JPA:PersistenceUnit) RETURN pu");
        assertThat(testResult.getRows()
                .size(), equalTo(1));
        List<? super PersistenceUnitDescriptor> persistenceUnitDescriptors = testResult.getColumn("pu");
        PersistenceUnitDescriptor persistenceUnitDescriptor = (PersistenceUnitDescriptor) persistenceUnitDescriptors.get(0);
        assertThat(persistenceUnitDescriptor.getName(), equalTo("persistence-unit"));
        assertThat(persistenceUnitDescriptor.getTransactionType(), equalTo("RESOURCE_LOCAL"));
        assertThat(persistenceUnitDescriptor.getDescription(), equalTo("description"));
        assertThat(persistenceUnitDescriptor.getJtaDataSource(), equalTo("jtaDataSource"));
        assertThat(persistenceUnitDescriptor.getNonJtaDataSource(), equalTo("nonJtaDataSource"));
        assertThat(persistenceUnitDescriptor.getProvider(), equalTo("provider"));
        assertThat(persistenceUnitDescriptor.getValidationMode(), equalTo("AUTO"));
        assertThat(persistenceUnitDescriptor.getSharedCacheMode(), equalTo("ENABLE_SELECTIVE"));
        assertThat(persistenceUnitDescriptor.getContains(), hasItem(typeDescriptor(entityClass)));
        Matcher<? super PropertyDescriptor> valueMatcher = valueDescriptor("stringProperty", equalTo("stringValue"));
        assertThat(persistenceUnitDescriptor.getProperties(), hasItem(valueMatcher));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jpa:ValidationModeMustBeExplicitlySpecified" if
     * it is not set.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void validationModeNotSpecifiedV20(Class<?> entityClass) throws Exception {
        validationModeMustBeExplicitlySpecified("jpa/2_0/minimal",  entityClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void validationModeNotSpecifiedV21(Class<?> entityClass) throws Exception {
        validationModeMustBeExplicitlySpecified("jpa/2_1/minimal",  entityClass);
    }

    /**
     * Verifies the constraint "jpa:ValidationModeMustBeExplicitlySpecified" if
     * it is set to AUTO.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void validationModeAutoV20(Class<?> entityClass) throws Exception {
        validationModeMustBeExplicitlySpecified("jpa/2_0/full",  entityClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void validationModeAutoV21(Class<?> entityClass) throws Exception {
        validationModeMustBeExplicitlySpecified("jpa/2_1/full",  entityClass);
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void validationModeAutoV32(Class<?> entityClass) throws Exception {
        validationModeMustBeExplicitlySpecified("jpa/3_2/full",  entityClass);
    }

    private void validationModeMustBeExplicitlySpecified(String path, Class<?> entityClass) throws RuleException {
        scanClassPathDirectory(new File(getClassesDirectory(entityClass), path));
        assertThat(validateConstraint("jpa:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults()
                .values());
        Matcher<Iterable<? super Result<Constraint>>> matcher = hasItem(result(constraint("jpa:ValidationModeMustBeExplicitlySpecified")));
        assertThat(constraintViolations, matcher);
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(false));
        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void validationModeSpecifiedV20(Class<?> entityClass) throws Exception {
        validationModeSpecified("jpa/2_0/validationmode", entityClass);
    }

    /**
     * Verifies the constraint "jpa:ValidationModeMustBeExplicitlySpecified"
     * for values NONE and CALLBACK.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxJpaEntity.class, JakartaJpaEntity.class})
    void validationModeSpecifiedV21(Class<?> entityClass) throws Exception {
        validationModeSpecified("jpa/2_1/validationmode", entityClass);
    }

    private void validationModeSpecified(String path, Class<?> entityClass) throws RuleException {
        scanClassPathDirectory(new File(getClassesDirectory(entityClass), path));
        assertThat(validateConstraint("jpa:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults()
                .values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(true));
        store.commitTransaction();
    }

    /**
     * Verifies a unique NamedQuery with property.
     *
     * @param queryName
     *         The query name.
     * @param query
     *         The query.
     * @param withQueryCount
     *         The number of nodes with the query attribute.
     * @param withoutQueryCount
     *         The number of nodes without the query attribute.
     */
    private void verifyUniqueRelation(String queryName, String query, int withQueryCount, int withoutQueryCount) {
        TestResult result = query("MATCH ()-[r:DEFINES]->(:JPA:NamedQuery) RETURN r");
        assertThat(result.getColumn("r")
                .size(), equalTo(2));
        assertThat(query("MATCH (q:JPA:NamedQuery {prop: 'value'}) RETURN q").getColumn("q")
                .size(), equalTo(2));
        result = query("MATCH ()-[:DEFINES]->(q:JPA:NamedQuery {name: '" + queryName + "', query: '" + query + "'}) RETURN q");
        if (withQueryCount == 0) {
            assertThat(result.getRows(), empty());
        } else {
            assertThat(result.getColumn("q")
                    .size(), equalTo(withQueryCount));
        }
        result = query("MATCH (q:JPA:NamedQuery {name: '" + queryName + "'}) WHERE q.query IS NULL RETURN q");
        if (withoutQueryCount == 0) {
            assertThat(result.getRows(), empty());
        } else {
            assertThat(result.getColumn("q")
                    .size(), equalTo(withoutQueryCount));
        }
    }
}
