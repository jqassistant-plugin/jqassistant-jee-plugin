package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import java.time.Clock;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class JakartaBeanWithInjectedClock {

    private final Clock clock;

    @Inject
    public JakartaBeanWithInjectedClock(Clock clock) {
        this.clock = clock;
    }

    public static class ClockProducer {
        @Produces
        public Clock clock() {
            return Clock.systemUTC();
        }
    }

}
