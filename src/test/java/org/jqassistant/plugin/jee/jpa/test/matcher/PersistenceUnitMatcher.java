package org.jqassistant.plugin.jee.jpa.test.matcher;

import org.jqassistant.plugin.jee.jpa.model.PersistenceUnitDescriptor;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * A matcher for {@link PersistenceUnitDescriptor}s.
 */
public class PersistenceUnitMatcher extends TypeSafeMatcher<PersistenceUnitDescriptor> {

    private final String name;

    public PersistenceUnitMatcher(String name) {
        this.name = name;
    }

    @Override
    protected boolean matchesSafely(PersistenceUnitDescriptor item) {
        return name.equals(item.getName());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a model unit with name ");
        description.appendValue(name);
    }

    /**
     * Return a {@link PersistenceUnitMatcher}.
     * 
     * @param name
     *            The expected name of the model unit.
     * @return The {@link PersistenceUnitMatcher}.
     */
    public static Matcher<? super PersistenceUnitDescriptor> persistenceUnitDescriptor(String name) {
        return new PersistenceUnitMatcher(name);
    }

}
