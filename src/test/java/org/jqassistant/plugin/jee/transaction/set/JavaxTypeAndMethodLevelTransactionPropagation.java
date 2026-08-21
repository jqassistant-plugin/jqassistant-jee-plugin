package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

@Transactional(value = Transactional.TxType.MANDATORY, rollbackOn = Exception.class)
public class JavaxTypeAndMethodLevelTransactionPropagation {
    public void transactionalMethodMandatoryAndRollbackOnException(){}

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNewAndDefaultRollback(){}

    @Transactional(value = Transactional.TxType.NEVER,  rollbackOn = RuntimeException.class)
    public void transactionalMethodNeverAndRollbackOnRuntimeException(){}

    @Transactional(rollbackOn = RuntimeException.class)
    public void transactionalMethodRequiredAndRollbackOnRuntimeException() {}
}
