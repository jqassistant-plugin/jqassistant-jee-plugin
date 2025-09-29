package org.jqassistant.plugin.jee.ejb.test.set.beans.jakarta;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;

/**
 * A bean with a scheduled timer.
 */
@Singleton
public class JakartaScheduledEJB {

    @Schedule
    public void invokeTimer() {}

}
