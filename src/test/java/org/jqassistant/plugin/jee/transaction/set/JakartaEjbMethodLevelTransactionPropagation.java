package org.jqassistant.plugin.jee.transaction.set;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless
public class JakartaEjbMethodLevelTransactionPropagation {
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void transactionalMethodMandatory(){}
}
