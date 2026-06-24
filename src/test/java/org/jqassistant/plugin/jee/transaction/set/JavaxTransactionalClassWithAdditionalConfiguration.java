package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

@Transactional(rollbackOn = Exception.class)
public class JavaxTransactionalClassWithAdditionalConfiguration {

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
