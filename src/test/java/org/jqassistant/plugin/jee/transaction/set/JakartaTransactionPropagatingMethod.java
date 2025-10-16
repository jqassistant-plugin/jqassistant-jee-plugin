package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaTransactionPropagatingMethod {

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void transactionalMethodRequiresNew(){}

    public void transactionalMethodRequired(){}
}
