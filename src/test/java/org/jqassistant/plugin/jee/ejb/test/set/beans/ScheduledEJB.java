package org.jqassistant.plugin.jee.ejb.test.set.beans;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * A bean with a scheduled timer.
 */
@Singleton
public class ScheduledEJB {

    @Schedule
    public void invokeTimer() {}

}
