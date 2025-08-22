package org.jqassistant.plugin.jee.cdi.test.set.beans.event.jakarta;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Reception;

import org.jqassistant.plugin.jee.cdi.test.set.beans.event.TestEvent;

/**
 * Custom CDI Event consumer.
 *
 * @author Aparna Chaudhary
 */
public class JakartaCustomEventConsumer {

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
