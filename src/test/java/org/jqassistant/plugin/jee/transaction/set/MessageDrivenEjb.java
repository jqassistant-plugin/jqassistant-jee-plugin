package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.MessageDriven;

/**
 * A message-driven EJB.
 */
@MessageDriven
public class MessageDrivenEjb {

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
