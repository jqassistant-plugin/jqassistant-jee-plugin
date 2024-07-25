package org.jqassistant.plugin.jee.jpa2.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.common.api.model.PropertyDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.jpa2.model.PersistenceUnitDescriptor;
import org.jqassistant.plugin.jee.jpa2.model.PersistenceXmlDescriptor;
import org.jqassistant.plugin.jee.jpa2.test.matcher.PersistenceUnitMatcher;
import org.jqassistant.plugin.jee.jpa2.test.set.entity.JpaEmbeddable;
import org.jqassistant.plugin.jee.jpa2.test.set.entity.JpaEntity;
import org.jqassistant.plugin.jee.jpa2.test.set.entity.SingleNamedQueryEntity;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

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
class Jpa2IT extends AbstractJavaPluginIT {

    public static final String SCHEMA_2_0 = "2.0";
    public static final String SCHEMA_2_1 = "2.1";

    /**
     * Verifies the concept `jpa2:Entity`.
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void entity() throws Exception {
        scanClasses(JpaEntity.class);
        assertThat(applyConcept("jpa2:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Type:Jpa:Entity) RETURN e").getColumn("e"), hasItem(typeDescriptor(JpaEntity.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "jpa2:Embeddable".
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void embeddable() throws Exception {
        scanClasses(JpaEmbeddable.class);
        assertThat(applyConcept("jpa2:Embeddable").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Type:Jpa:Embeddable) RETURN e").getColumn("e"), hasItem(typeDescriptor(JpaEmbeddable.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "jpa2:Embedded".
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void embedded() throws Exception {
        scanClasses(JpaEntity.class);
        assertThat(applyConcept("jpa2:Embedded").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> members = query("MATCH (e:Jpa:Embedded) RETURN e").getColumn("e");
        assertThat(members, hasItem(fieldDescriptor(JpaEntity.class, "embedded")));
        assertThat(members, hasItem(methodDescriptor(JpaEntity.class, "getEmbedded")));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "jpa2:EmbeddedId".
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void embeddedId() throws Exception {
        scanClasses(JpaEntity.class);
        assertThat(applyConcept("jpa2:EmbeddedId").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Object> members = query("MATCH (e:Jpa:EmbeddedId) RETURN e").getColumn("e");
        assertThat(members, hasItem(fieldDescriptor(JpaEntity.class, "id")));
        assertThat(members, hasItem(methodDescriptor(JpaEntity.class, "getId")));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "jpa2:NamedQuery".
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void namedQueries() throws Exception {
        scanClasses(JpaEntity.class, SingleNamedQueryEntity.class);
        assertThat(applyConcept("jpa2:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyNamedQuery(JpaEntity.class, JpaEntity.TESTQUERY_NAME, JpaEntity.TESTQUERY_QUERY);
        verifyNamedQuery(SingleNamedQueryEntity.class, SingleNamedQueryEntity.TESTQUERY_NAME, SingleNamedQueryEntity.TESTQUERY_QUERY);
        store.commitTransaction();
    }

    /**
     * Verify the result
     *
     * @param entity The entity class to verify.
     * @param name   The name of the defined query.
     * @param query  The query.
     */
    private void verifyNamedQuery(Class<?> entity, String name, String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("entity", entity.getName());
        TestResult result = query("MATCH (e:Entity {fqn:$entity})-[:DEFINES]->(n:Jpa:NamedQuery) RETURN n.name as name, n.query as query", params);
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        Map<String, Object> row = rows.get(0);
        assertThat((String) row.get("name"), equalTo(name));
        assertThat((String) row.get("query"), equalTo(query));
    }

    /**
     * Verifies the uniqueness of concept "jpa2:NamedQuery" with keeping existing properties.
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void namedQueryUniqueDifferentQuery() throws Exception {
        scanClasses(JpaEntity.class, SingleNamedQueryEntity.class);
        assertThat(applyConcept("jpa2:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Jpa:Entity {name: 'JpaEntity'}) CREATE (e)-[:DEFINES]->(n:Jpa:NamedQuery {name: 'namedQueries', prop: 'value', query: 'foo'}) RETURN n").getColumn("n")
                                                                                                                                                                           .size(), equalTo(1));
        assertThat(query("MATCH (e:Jpa:Entity {name: 'SingleNamedQueryEntity'}) CREATE (e)-[:DEFINES]->(n:Jpa:NamedQuery {name: 'namedQuery', prop: 'value', query: 'foo'}) RETURN n").getColumn("n")
                                                                                                                                                                                      .size(), equalTo(1));
        assertThat(query("CREATE (n:Jpa:NamedQuery {name: 'otherQuery', query: 'SELECT e'}) RETURN n").getColumn("n")
                                                                                                      .size(), equalTo(1));
        verifyUniqueRelation(JpaEntity.TESTQUERY_NAME, JpaEntity.TESTQUERY_QUERY, 2, 0, 0);
        verifyUniqueRelation(SingleNamedQueryEntity.TESTQUERY_NAME, SingleNamedQueryEntity.TESTQUERY_QUERY, 2, 0, 0);
        store.commitTransaction();
        assertThat(applyConcept("jpa2:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyUniqueRelation(JpaEntity.TESTQUERY_NAME, JpaEntity.TESTQUERY_QUERY, 2, 1, 0);
        verifyUniqueRelation(SingleNamedQueryEntity.TESTQUERY_NAME, SingleNamedQueryEntity.TESTQUERY_QUERY, 2, 1, 0);
        store.commitTransaction();
    }

    /**
     * Verifies the uniqueness of concept "jpa2:NamedQuery" with keeping existing properties.
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void namedQueryUniqueSameQuery() throws Exception {
        scanClasses(JpaEntity.class, SingleNamedQueryEntity.class);
        assertThat(applyConcept("jpa2:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Jpa:Entity {name: 'JpaEntity'}) CREATE (e)-[:DEFINES]->(n:Jpa:NamedQuery {name: 'namedQueries', prop: 'value', query: 'SELECT e FROM JpaEntity e'}) RETURN n").getColumn("n")
                                                                                                                                                                                                 .size(), equalTo(1));
        assertThat(query("MATCH (e:Jpa:Entity {name: 'SingleNamedQueryEntity'}) CREATE (e)-[:DEFINES]->(n:Jpa:NamedQuery {name: 'namedQuery', prop: 'value', query: 'SELECT e FROM SingleNamedQueryEntity e'}) RETURN n").getColumn("n")
                                                                                                                                                                                                                         .size(), equalTo(1));
        assertThat(query("CREATE (n:Jpa:NamedQuery {name: 'otherQuery', query: 'SELECT e'}) RETURN n").getColumn("n")
                                                                                                      .size(), equalTo(1));
        verifyUniqueRelation(JpaEntity.TESTQUERY_NAME, JpaEntity.TESTQUERY_QUERY, 2, 1, 0);
        verifyUniqueRelation(SingleNamedQueryEntity.TESTQUERY_NAME, SingleNamedQueryEntity.TESTQUERY_QUERY, 2, 1, 0);
        store.commitTransaction();
        assertThat(applyConcept("jpa2:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyUniqueRelation(JpaEntity.TESTQUERY_NAME, JpaEntity.TESTQUERY_QUERY, 2, 1, 0);
        verifyUniqueRelation(SingleNamedQueryEntity.TESTQUERY_NAME, SingleNamedQueryEntity.TESTQUERY_QUERY, 2, 1, 0);
        store.commitTransaction();
    }

    /**
     * Verifies the uniqueness of concept "jpa2:NamedQuery" with keeping existing properties.
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void namedQueryUniqueWithoutQuery() throws Exception {
        scanClasses(JpaEntity.class, SingleNamedQueryEntity.class);
        assertThat(applyConcept("jpa2:Entity").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Jpa:Entity {name: 'JpaEntity'}) CREATE (e)-[:DEFINES]->(n:Jpa:NamedQuery {name: 'namedQueries', prop: 'value'}) RETURN n").getColumn("n")
                                                                                                                                                             .size(), equalTo(1));
        assertThat(query("MATCH (e:Jpa:Entity {name: 'SingleNamedQueryEntity'}) CREATE (e)-[:DEFINES]->(n:Jpa:NamedQuery {name: 'namedQuery', prop: 'value'}) RETURN n").getColumn("n")
                                                                                                                                                                        .size(), equalTo(1));
        assertThat(query("CREATE (n:Jpa:NamedQuery {name: 'otherQuery', query: 'SELECT e'}) RETURN n").getColumn("n")
                                                                                                      .size(), equalTo(1));
        verifyUniqueRelation(JpaEntity.TESTQUERY_NAME, JpaEntity.TESTQUERY_QUERY, 2, 0, 1);
        verifyUniqueRelation(SingleNamedQueryEntity.TESTQUERY_NAME, SingleNamedQueryEntity.TESTQUERY_QUERY, 2, 0, 1);
        store.commitTransaction();
        assertThat(applyConcept("jpa2:NamedQuery").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        verifyUniqueRelation(JpaEntity.TESTQUERY_NAME, JpaEntity.TESTQUERY_QUERY, 2, 1, 0);
        verifyUniqueRelation(SingleNamedQueryEntity.TESTQUERY_NAME, SingleNamedQueryEntity.TESTQUERY_QUERY, 2, 1, 0);
        store.commitTransaction();
    }

    /**
     * Verifies scanning of persistence descriptors.
     *
     * @throws IOException If the test fails.
     */
    @Test
    void fullPersistenceDescriptorV20() throws IOException {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_0/full"));
        store.beginTransaction();
        TestResult testResult = query("MATCH (p:Jpa:Persistence:Xml) RETURN p");
        assertThat(testResult.getRows().size(), equalTo(1));
        List<? super PersistenceXmlDescriptor> persistenceDescriptors = testResult.getColumn("p");
        PersistenceXmlDescriptor persistenceXmlDescriptor = (PersistenceXmlDescriptor) persistenceDescriptors.get(0);
        assertThat(persistenceXmlDescriptor.getVersion(), equalTo(SCHEMA_2_0));
        List<PersistenceUnitDescriptor> persistenceUnits = persistenceXmlDescriptor.getContains();
        assertThat(persistenceUnits, hasItem(PersistenceUnitMatcher.persistenceUnitDescriptor("persistence-unit")));
        store.commitTransaction();
    }

    @Test
    void fullPersistenceDescriptorV21() throws IOException {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_1/full"));
        store.beginTransaction();
        TestResult testResult = query("MATCH (p:Jpa:Persistence:Xml) RETURN p");
        assertThat(testResult.getRows().size(), equalTo(1));
        List<? super PersistenceXmlDescriptor> persistenceDescriptors = testResult.getColumn("p");
        PersistenceXmlDescriptor persistenceXmlDescriptor = (PersistenceXmlDescriptor) persistenceDescriptors.get(0);
        assertThat(persistenceXmlDescriptor.getVersion(), equalTo(SCHEMA_2_1));
        List<PersistenceUnitDescriptor> persistenceUnits = persistenceXmlDescriptor.getContains();
        assertThat(persistenceUnits, hasItem(PersistenceUnitMatcher.persistenceUnitDescriptor("persistence-unit")));
        store.commitTransaction();
    }

    /**
     * Verifies scanning of persistence unit descriptors.
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void fullPersistenceUnitDescriptorV21() throws IOException {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_1/full"));
        store.beginTransaction();
        TestResult testResult = query("MATCH (pu:Jpa:PersistenceUnit) RETURN pu");
        assertThat(testResult.getRows().size(), equalTo(1));
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
        assertThat(persistenceUnitDescriptor.getContains(), hasItem(typeDescriptor(JpaEntity.class)));
        Matcher<? super PropertyDescriptor> valueMatcher = valueDescriptor("stringProperty", equalTo("stringValue"));
        assertThat(persistenceUnitDescriptor.getProperties(), hasItem(valueMatcher));
        store.commitTransaction();
    }

    @Test
    void fullPersistenceUnitDescriptorV20() throws IOException {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_0/full"));
        store.beginTransaction();
        TestResult testResult = query("MATCH (pu:Jpa:PersistenceUnit) RETURN pu");
        assertThat(testResult.getRows().size(), equalTo(1));
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
        assertThat(persistenceUnitDescriptor.getContains(), hasItem(typeDescriptor(JpaEntity.class)));
        Matcher<? super PropertyDescriptor> valueMatcher = valueDescriptor("stringProperty", equalTo("stringValue"));
        assertThat(persistenceUnitDescriptor.getProperties(), hasItem(valueMatcher));
        store.commitTransaction();
    }

    /**
     * Verifies scanning of persistence descriptors.
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void minimalPersistenceDescriptorV21() throws IOException {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_1/minimal"));
        store.beginTransaction();
        TestResult testResult = query("MATCH (p:Jpa:Persistence:Xml) RETURN p");
        assertThat(testResult.getRows().size(), equalTo(1));
        List<? super PersistenceXmlDescriptor> persistenceDescriptors = testResult.getColumn("p");
        PersistenceXmlDescriptor persistenceXmlDescriptor = (PersistenceXmlDescriptor) persistenceDescriptors.get(0);
        assertThat(persistenceXmlDescriptor.getVersion(), equalTo(SCHEMA_2_1));
        List<PersistenceUnitDescriptor> persistenceUnits = persistenceXmlDescriptor.getContains();
        assertThat(persistenceUnits, hasItem(PersistenceUnitMatcher.persistenceUnitDescriptor("persistence-unit")));
        store.commitTransaction();
    }

    @Test
    void minimalPersistenceDescriptorV20() throws IOException {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_0/minimal"));
        store.beginTransaction();
        TestResult testResult = query("MATCH (p:Jpa:Persistence:Xml) RETURN p");
        assertThat(testResult.getRows().size(), equalTo(1));
        List<? super PersistenceXmlDescriptor> persistenceDescriptors = testResult.getColumn("p");
        PersistenceXmlDescriptor persistenceXmlDescriptor = (PersistenceXmlDescriptor) persistenceDescriptors.get(0);
        assertThat(persistenceXmlDescriptor.getVersion(), equalTo(SCHEMA_2_0));
        List<PersistenceUnitDescriptor> persistenceUnits = persistenceXmlDescriptor.getContains();
        assertThat(persistenceUnits, hasItem(PersistenceUnitMatcher.persistenceUnitDescriptor("persistence-unit")));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jpa2:ValidationModeMustBeExplicitlySpecified" if
     * it is not set.
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void validationModeNotSpecifiedV20() throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_0/minimal"));
        assertThat(validateConstraint("jpa2:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        Matcher<Iterable<? super Result<Constraint>>> matcher = hasItem(result(constraint("jpa2:ValidationModeMustBeExplicitlySpecified")));
        assertThat(constraintViolations, matcher);
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(false));
        store.commitTransaction();
    }

    @Test
    void validationModeNotSpecifiedV21() throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_1/minimal"));
        assertThat(validateConstraint("jpa2:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        Matcher<Iterable<? super Result<Constraint>>> matcher = hasItem(result(constraint("jpa2:ValidationModeMustBeExplicitlySpecified")));
        assertThat(constraintViolations, matcher);
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(false));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jpa2:ValidationModeMustBeExplicitlySpecified" if
     * it is set to AUTO.
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void validationModeAutoV20() throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_0/full"));
        assertThat(validateConstraint("jpa2:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        Matcher<Iterable<? super Result<Constraint>>> matcher = hasItem(result(constraint("jpa2:ValidationModeMustBeExplicitlySpecified")));
        assertThat(constraintViolations, matcher);
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(false));
        store.commitTransaction();
    }

    @Test
    void validationModeAutoV21() throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_1/full"));
        assertThat(validateConstraint("jpa2:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(FAILURE));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        Matcher<Iterable<? super Result<Constraint>>> matcher = hasItem(result(constraint("jpa2:ValidationModeMustBeExplicitlySpecified")));
        assertThat(constraintViolations, matcher);
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(false));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jpa2:ValidationModeMustBeExplicitlySpecified"
     * for values NONE and CALLBACK.
     *
     * @throws IOException                                           If the test fails.
     */
    @Test
    void validationModeSpecifiedV21() throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_1/validationmode"));
        assertThat(validateConstraint("jpa2:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(true));
        store.commitTransaction();
    }

    @Test
    void validationModeSpecifiedV20() throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(JpaEntity.class), "jpa2/2_0/validationmode"));
        assertThat(validateConstraint("jpa2:ValidationModeMustBeExplicitlySpecified").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations.size(), equalTo(1));
        Result<Constraint> constraintResult = constraintViolations.get(0);
        assertThat(constraintResult.isEmpty(), equalTo(true));
        store.commitTransaction();
    }

    /**
     * Verifies a unique NamedQuery with property.
     * @param queryName The query name.
     * @param query The query.
     * @param relationCount The number of :DEFINES relations to named query nodes.
     * @param withQueryCount The number of nodes with the query attribute.
     * @param withoutQueryCount The number of nodes without the query attribute.
     */
    private void verifyUniqueRelation(String queryName, String query, int relationCount, int withQueryCount, int withoutQueryCount) {
        TestResult result = query("MATCH ()-[r:DEFINES]->(:Jpa:NamedQuery) RETURN r");
        if (relationCount == 0) {
            assertThat(result.getRows(), empty());
        } else {
            assertThat(result.getColumn("r").size(), equalTo(relationCount));
        }
        assertThat(query("MATCH (q:Jpa:NamedQuery {prop: 'value'}) RETURN q").getColumn("q").size(), equalTo(2));
        result = query("MATCH ()-[:DEFINES]->(q:Jpa:NamedQuery {name: '" + queryName + "', query: '" + query + "'}) RETURN q");
        if (withQueryCount == 0) {
            assertThat(result.getRows(), empty());
        } else {
            assertThat(result.getColumn("q").size(), equalTo(withQueryCount));
        }
        result = query("MATCH (q:Jpa:NamedQuery {name: '" + queryName + "'}) WHERE q.query IS NULL RETURN q");
        if (withoutQueryCount == 0) {
            assertThat(result.getRows(), empty());
        } else {
            assertThat(result.getColumn("q").size(), equalTo(withoutQueryCount));
        }
    }
}
