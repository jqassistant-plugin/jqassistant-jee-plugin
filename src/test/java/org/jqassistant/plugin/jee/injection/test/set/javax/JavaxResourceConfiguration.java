package org.jqassistant.plugin.jee.injection.test.set.javax;

import lombok.RequiredArgsConstructor;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@ApplicationScoped
public class JavaxResourceConfiguration {

    @Produces
    @Resource
    Queue fieldProducedResource;

    @Resource
    Topic directlyMethodProducedResource;

    @Resource
    ConnectionFactory indirectlyMethodProducedResource;

    @Resource
    TransactionManager innerResource;

    @Resource
    UserTransaction someOtherResource;

    @Produces
    Topic produceDirectlyMethodProducedResource() {
        return directlyMethodProducedResource;
    }

    @Produces
    ConnectionFactory produceIndirectlyMethodProducedResource() {
        return internalProduceIndirectlyMethodProducedResource();
    }

    private ConnectionFactory internalProduceIndirectlyMethodProducedResource() {
        return indirectlyMethodProducedResource;
    }

    private UserTransaction getSomeOtherResource() {
        return someOtherResource;
    }

    @RequiredArgsConstructor
    private static class TransactionHandler {
        private final TransactionManager transactionManager;
    }

    @Produces
    TransactionHandler produceComplexResource() {
        return new TransactionHandler(innerResource);
    }

}
