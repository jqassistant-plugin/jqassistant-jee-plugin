package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.Stateful;

/**
 * A stateful EJB.
 */
@Stateful
public class StatefulEjb {

    public void transactionalMethod(){
    }

    private void privateMethod() {
    }

    private void callingPrivateMethod() {
        privateMethod(); // Private methods are not transactional and may be called.
    }
}
