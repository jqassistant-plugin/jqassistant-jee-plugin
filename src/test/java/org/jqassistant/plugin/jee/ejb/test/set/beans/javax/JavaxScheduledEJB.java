package org.jqassistant.plugin.jee.ejb.test.set.beans.javax;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * A bean with a scheduled timer.
 */
@Singleton
public class JavaxScheduledEJB {

    @Schedule
    public void invokeTimer() {}

}
