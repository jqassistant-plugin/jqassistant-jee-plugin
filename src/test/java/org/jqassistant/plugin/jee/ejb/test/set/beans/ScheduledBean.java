package org.jqassistant.plugin.jee.ejb.test.set.beans;

import javax.ejb.Schedule;

/**
 * A bean with a scheduled timer.
 */
public class ScheduledBean {

    @Schedule
    public void invokeTimer() {}

}
