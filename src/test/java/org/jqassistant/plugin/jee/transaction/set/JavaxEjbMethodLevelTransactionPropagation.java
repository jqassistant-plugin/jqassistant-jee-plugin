package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class JavaxEjbMethodLevelTransactionPropagation {

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public  void transactionalMethodMandatory(){}
}
