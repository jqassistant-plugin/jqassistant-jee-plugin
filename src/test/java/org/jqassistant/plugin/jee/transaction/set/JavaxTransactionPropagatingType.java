package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.transaction.Transactional;

@Transactional(value = Transactional.TxType.MANDATORY)
public class JavaxTransactionPropagatingType {
    public void transactionalMethodMandatory(){}

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNew(){}

    @TransactionAttribute(value = TransactionAttributeType.NEVER)
    public void transactionalMethodNever(){}
}
