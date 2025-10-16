package org.jqassistant.plugin.jee.transaction.set;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.transaction.Transactional;

@Transactional(value = Transactional.TxType.MANDATORY)
public class JakartaTransactionPropagatingType {
    public void transactionalMethodMandatory(){}

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNew(){}

    @TransactionAttribute(value = TransactionAttributeType.NEVER)
    public void transactionalMethodNever(){}
}
