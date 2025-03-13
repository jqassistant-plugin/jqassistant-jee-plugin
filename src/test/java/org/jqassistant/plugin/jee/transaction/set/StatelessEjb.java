package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.Stateless;

/**
 * A stateless EJB.
 */
@Stateless
public class StatelessEjb {

    public void transactionalMethod(){
    }

    private void privateMethod() {
    }

    private void callingPrivateMethod() {
        privateMethod(); // Private methods are not transactional and may be called.
    }
}
