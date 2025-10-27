package org.jqassistant.plugin.jee.transaction.set;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JakartaEjbTypeAndMethodLevelTransactionPropagation {
    public void transactionalMethodMandatory(){}

    @TransactionAttribute(value = TransactionAttributeType.NEVER)
    public void transactionalMethodNever(){}

    @TransactionAttribute
    public void transactionalMethodRequired(){}
}
