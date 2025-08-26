package org.jqassistant.plugin.jee.transaction.set;

import jakarta.ejb.Stateless;

/**
 * A stateless EJB.
 */
@Stateless
public class JakartaStatelessEjb {

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
