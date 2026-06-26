package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

public class JakartaTransactionalMethodWithAdditionalConfiguration {

    @Transactional(rollbackOn = Exception.class)
    public void transactionalMethodWithRequiredSemanticsAndRollbackOnException() {

    }

    @Transactional(value = Transactional.TxType.NEVER, rollbackOn = Exception.class)
    public void transactionalMethodWithNeverSemanticsAndRollbackOnException() {}

    // This transaction semantics is always compatible with the caller.
    // Calling it within the same bean should lead to finding anyway as it contains an additional configuration attribute.
    @Transactional(value = Transactional.TxType.SUPPORTS, rollbackOn = Exception.class)
    public void transactionalMethodWithSupportsSemanticsAndRollbackOnException() {}

    private void privateCallingTransactional() {
        transactionalMethodWithNeverSemanticsAndRollbackOnException();
    }

    @Transactional
    public void requiredTransactionalCallingMethodWithAdditionalConfigurationTransitively() {
        privateCallingTransactional();
    }

    @Transactional
    public void transactionalMethodWithRequiredSemanticsCallingMethodsWithAdditionalConfiguration() {
        transactionalMethodWithRequiredSemanticsAndRollbackOnException();
        transactionalMethodWithNeverSemanticsAndRollbackOnException();
        transactionalMethodWithSupportsSemanticsAndRollbackOnException();
    }
}
