package org.jqassistant.plugin.jee.cdi.test.set.beans.event.javax;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

import org.jqassistant.plugin.jee.cdi.test.set.beans.event.TestEvent;

/**
 * Custom CDI Event consumer.
 *
 * @author Aparna Chaudhary
 */
public class JavaxCustomEventConsumer {

    /**
     * Observer for CDI Event of type {@link TestEvent}
     *
     * @param testEvent
     *            test event
     */
    public void onTestEvent(@Observes(notifyObserver = Reception.IF_EXISTS) final TestEvent testEvent) {
        //do something with test event
    }

}
