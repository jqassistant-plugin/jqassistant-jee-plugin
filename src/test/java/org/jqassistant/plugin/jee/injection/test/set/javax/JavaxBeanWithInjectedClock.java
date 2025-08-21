package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.time.Clock;

public class JavaxBeanWithInjectedClock {

    private final Clock clock;

    @Inject
    public JavaxBeanWithInjectedClock(Clock clock) {
        this.clock = clock;
    }

    public static class ClockProducer {
        @Produces
        public Clock clock() {
            return Clock.systemUTC();
        }
    }

}
