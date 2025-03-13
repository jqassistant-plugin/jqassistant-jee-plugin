package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

public class JakartaTransactionalMethod {

    @Transactional
    public void transactionalMethod() {

    }

    public void nonTransactionalMethod() {
    }

    private void callingTransactional() {
        transactionalMethod();
    }

    @Transactional
    private void privateMethod() {
    }

    private void callingPrivateMethod() {
        privateMethod(); // Private methods are not transactional and may be called.
    }

}
