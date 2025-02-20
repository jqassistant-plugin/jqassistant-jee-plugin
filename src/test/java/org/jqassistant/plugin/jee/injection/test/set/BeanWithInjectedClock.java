package org.jqassistant.plugin.jee.injection.test.set;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.time.Clock;

public class BeanWithInjectedClock {

    private final Clock clock;

    @Inject
    public BeanWithInjectedClock(Clock clock) {
        this.clock = clock;
    }

    public static class ClockProducer {
        @Produces
        public Clock clock() {
            return Clock.systemUTC();
        }
    }

}
