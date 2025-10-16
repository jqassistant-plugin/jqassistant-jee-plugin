package org.jqassistant.plugin.jee.transaction.set;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JakartaTransactionPropagatingEjb {
    public void transactionalMethodMandatory(){}
}
