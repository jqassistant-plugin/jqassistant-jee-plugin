package org.jqassistant.plugin.jee.ejb.test.set.beans.javax;

import javax.ejb.Schedule;

/**
 * A bean with a scheduled timer.
 */
public class JavaxScheduledBean {

    @Schedule
    public void invokeTimer() {}

}
