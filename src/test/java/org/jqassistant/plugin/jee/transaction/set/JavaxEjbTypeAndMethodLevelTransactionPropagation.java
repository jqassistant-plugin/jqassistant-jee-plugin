package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JavaxEjbTypeAndMethodLevelTransactionPropagation {
    public void transactionalMethodMandatory(){}

    @TransactionAttribute(value = TransactionAttributeType.NEVER)
    public void transactionalMethodNever(){}

    @TransactionAttribute
    public void transactionalMethodRequired(){}
}
