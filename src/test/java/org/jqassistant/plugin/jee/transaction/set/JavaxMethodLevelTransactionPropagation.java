package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

@Transactional
public class JavaxMethodLevelTransactionPropagation {

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNew(){}

    public void transactionalMethodRequired(){}
}
