package org.jqassistant.plugin.jee.ejb.test.set.beans.jakarta;


import jakarta.ejb.Schedule;

/**
 * A bean with a scheduled timer.
 */
public class JakartaScheduledBean {

    @Schedule
    public void invokeTimer() {}

}
