package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.Singleton;

/**
 * A singleton EJB.
 */
@Singleton
public class JavaxSingletonEjb {

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
