package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JavaxTransactionPropagatingEjb {
    public void transactionalMethodMandatory(){}
}
