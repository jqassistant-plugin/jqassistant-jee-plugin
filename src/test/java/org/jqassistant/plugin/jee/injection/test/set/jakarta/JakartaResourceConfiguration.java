package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import lombok.RequiredArgsConstructor;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import jakarta.jms.Topic;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

@ApplicationScoped
public class JakartaResourceConfiguration {

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

    @RequiredArgsConstructor
    private static class TransactionHandler {
        private final TransactionManager transactionManager;
    }

    @Produces
    TransactionHandler produceComplexResource() {
        return new TransactionHandler(innerResource);
    }

    UserTransaction getSomeOtherResource() {
        return someOtherResource;
    }

}
