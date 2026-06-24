package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional(rollbackOn = Exception.class)
public class JakartaTransactionalClassWithAdditionalConfiguration {

    public void transactionalMethodWithRequiredSemanticsAndRollbackOnException() {

    }

    @Transactional(value = Transactional.TxType.NEVER, rollbackOn = Exception.class)
    public void transactionalMethodWithNeverSemanticsAndRollbackOnException() {

    }

    private void privateCallingTransactional() {
        transactionalMethodWithNeverSemanticsAndRollbackOnException();
    }

    public void requiredTransactionalCallingMethodWithAdditionalConfigurationTransitively() {
        privateCallingTransactional();
    }

    public void transactionalMethodWithRequiredSemanticsCallingMethodsWithAdditionalConfiguration() {
        transactionalMethodWithRequiredSemanticsAndRollbackOnException();
        transactionalMethodWithNeverSemanticsAndRollbackOnException();
    }
}
