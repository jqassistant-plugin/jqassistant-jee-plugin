package org.jqassistant.plugin.jee.transaction.set;

import jakarta.ejb.MessageDriven;

/**
 * A message-driven EJB.
 */
@MessageDriven
public class JakartaMessageDrivenEjb {

    public void transactionalMethod(){
    }

    private void callingTransactional() {
        transactionalMethod();
    }

    private void privateMethod() {
    }

    private void callingPrivateMethod() {
        privateMethod(); // Private methods are not transactional and may be called.
    }

}
