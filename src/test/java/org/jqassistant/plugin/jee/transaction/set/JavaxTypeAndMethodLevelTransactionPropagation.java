package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

@Transactional(value = Transactional.TxType.MANDATORY)
public class JavaxTypeAndMethodLevelTransactionPropagation {
    public void transactionalMethodMandatory(){}

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNew(){}

    @Transactional(value = Transactional.TxType.NEVER)
    public void transactionalMethodNever(){}
}
