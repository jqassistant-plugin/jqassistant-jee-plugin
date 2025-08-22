package org.jqassistant.plugin.jee.cdi.test.set.beans.event.javax;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.event.TestEvent;

/**
 * Custom CDI Event Producer.
 *
 * @author Aparna Chaudhary
 */
public class JavaxCustomEventProducer {

    @Inject
    Event<TestEvent> todolistEvent;

    /**
     * Fires CDI Event of type {@link TestEvent}
     *
     * @param testEvent
     *            test event
     * @return test event
     */
    public TestEvent create(TestEvent testEvent) {
        todolistEvent.fire(testEvent);
        return testEvent;
    }

}
