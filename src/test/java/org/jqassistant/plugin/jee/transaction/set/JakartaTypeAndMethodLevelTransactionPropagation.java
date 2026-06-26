package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = Exception.class)
public class JakartaTypeAndMethodLevelTransactionPropagation {
    public void transactionalMethodMandatoryAndRollbackOnException(){}

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNewAndDefaultRollback(){}

    @Transactional(value = Transactional.TxType.NEVER,  rollbackOn = RuntimeException.class)
    public void transactionalMethodNeverAndRollbackOnRuntimeException(){}

    @Transactional(rollbackOn = RuntimeException.class)
    public void transactionalMethodRequiredAndRollbackOnRuntimeException() {}
}
