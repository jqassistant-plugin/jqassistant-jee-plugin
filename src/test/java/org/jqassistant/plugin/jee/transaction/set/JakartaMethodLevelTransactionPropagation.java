package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaMethodLevelTransactionPropagation {

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNewAndDefaultRollback(){}

    public void transactionalMethodRequiredAndDefaultRollback(){}

    @Transactional(value = Transactional.TxType.NEVER, rollbackOn = RuntimeException.class)
    public void transactionalMethodNeverAndRollbackOnRuntimeException(){}

    @Transactional(rollbackOn = RuntimeException.class)
    public void transactionalMethodRequiredAndRollbackOnRuntimeException() {}
}
