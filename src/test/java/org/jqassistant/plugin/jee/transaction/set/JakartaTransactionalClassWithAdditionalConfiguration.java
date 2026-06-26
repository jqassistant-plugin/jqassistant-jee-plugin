package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional(rollbackOn = Exception.class)
public class JakartaTransactionalClassWithAdditionalConfiguration {

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

    public void requiredTransactionalCallingMethodWithAdditionalConfigurationTransitively() {
        privateCallingTransactional();
    }

    public void transactionalMethodWithRequiredSemanticsCallingMethodsWithAdditionalConfiguration() {
        transactionalMethodWithRequiredSemanticsAndRollbackOnException();
        transactionalMethodWithNeverSemanticsAndRollbackOnException();
        transactionalMethodWithSupportsSemanticsAndRollbackOnException();
    }
}
