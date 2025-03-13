package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaTransactionalClass {

    public void transactionalMethod(){
    }

    private void privateMethod() {
    }

    private void callingPrivateMethod() {
        privateMethod(); // Private methods are not transactional and may be called.
    }

}
